/**
 * Copyright 2019 Distributed Systems Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package SimBlock.node;

import static SimBlock.settings.NetworkConfiguration.PARTITION_REGION;
import static SimBlock.settings.SimulationConfiguration.*;
import static SimBlock.simulator.Main.*;
import static SimBlock.simulator.Network.*;
import static SimBlock.simulator.Simulator.*;
import static SimBlock.simulator.Timer.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import SimBlock.block.Block;
import SimBlock.node.consensusAlgo.AbstractConsensusAlgo;
import SimBlock.node.routingTable.AbstractRoutingTable;
//import SimBlock.node.routingTable.BitcoinCoreTable;
import SimBlock.simulator.Main;
import SimBlock.task.AbstractMessageTask;
import SimBlock.task.BlockMessageTask;
import SimBlock.task.InvMessageTask;
import SimBlock.task.RecMessageTask;
import SimBlock.task.AbstractMintingTask;

public class Node {   // All the parameters of node
	public int nodeID;    // Each run will assign a unique ID to each node
	public int region;    // The number of the region where the node is located
	public long miningPower;  // Define the mining power of this node
//	public AbstractRoutingTable routingTable_partition;
	public AbstractRoutingTable routingTable;
	//private BitcoinCoreTable routingTable;
	public AbstractConsensusAlgo consensusAlgo;
	public Block block;
	public Set<Block> orphans = new HashSet<Block>();
	public AbstractMintingTask mintingTask = null;
	public boolean sendingBlock = false;
	public ArrayList<RecMessageTask> messageQue = new ArrayList<RecMessageTask>();
	public Set<Block> downloadingBlocks = new HashSet<Block>();
	public boolean partition_flag = false;


	public long processingTime = 200;

	public Node(int nodeID,int nConnection ,int region, long miningPower, String routingTableName, String consensusAlgoName){
		this.nodeID = nodeID;     // Each run will assign a unique ID to each node
		this.region = region;     // The number of the region where the node is located
		this.miningPower = miningPower;   // Define the mining power of this node
		//if(this.nodeID ==50) this.miningPower = 10;
		if(region == PARTITION_REGION) this.partition_flag = true;

		try {     // Call routing table class and consensus mechanism algorithm class
			this.routingTable = (AbstractRoutingTable) Class.forName(routingTableName).getConstructor(Node.class).newInstance(this);
//			this.routingTable_partition = (AbstractRoutingTable) Class.forName(routingTableName).getConstructor(Node.class).newInstance(this);
			//this.routingTable = (BitcoinCoreTable) Class.forName(routingTableName).getConstructor(Node.class).newInstance(this);
			this.consensusAlgo = (AbstractConsensusAlgo) Class.forName(consensusAlgoName).getConstructor(Node.class).newInstance(this);
			this.setnConnection(nConnection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean getNodePartitionFlag(){ return this.partition_flag; }
	public int getNodeID(){ return this.nodeID; }
	public int getRegion(){ return this.region; }
	public long getMiningPower(){ return this.miningPower; }
	public AbstractConsensusAlgo getConsensusAlgo() { return this.consensusAlgo; }
	public AbstractRoutingTable getRoutingTable(){ return this.routingTable; }
	//public BitcoinCoreTable getRoutingTable(){ return this.routingTable; }
	public Block getBlock(){ return this.block; }
	public Set<Block> getOrphans(){ return this.orphans; }

	public int getnConnection(){ return this.routingTable.getnConnection(); }
	public void setnConnection(int nConnection){ this.routingTable.setnConnection(nConnection); }
	public ArrayList<Node> getNeighbors(){ return this.routingTable.getNeighbors(); }
	public boolean addNeighbor(Node node){ return this.routingTable.addNeighbor(node); }
	public boolean removeNeighbor(Node node){ return this.routingTable.removeNeighbor(node); }

	public void joinNetwork(){
		this.routingTable.initTable();
	}
//	public void joinNetwork_partition(){
//		this.routingTable_partition.initTable();
//	}

	public void  genesisBlock(){
		Block genesis = this.consensusAlgo.genesisBlock();
		this.receiveBlock(genesis);
	}

	public void addToChain(Block newBlock) {    // Add block to the  blockchain
		if(this.mintingTask != null){
			removeTask(this.mintingTask);
			this.mintingTask = null;
		}
		this.block = newBlock;
		printAddBlock(newBlock);
		arriveBlock(newBlock, this);
	}

	private void printAddBlock(Block newBlock){    // Print the information of all nodes in each block to the info file
		OUT_JSON_FILE.print("{");
		OUT_JSON_FILE.print(	"\"kind\":\"add-block\",");
		OUT_JSON_FILE.print(	"\"content\":{");
		OUT_JSON_FILE.print(		"\"timestamp\":" + getCurrentTime() + ",");
		OUT_JSON_FILE.print(		"\"node-id\":" + this.getNodeID() + ",");

		//System.out.println("timestamp:"+getCurrentTime()+", node-id:"+this.getNodeID()+", block-id:"+newBlock.getId());
		try {
			FileWriter fw2 = new FileWriter(new File(OUT_FILE_URI.resolve("./graph/"+ "NodeInfo" +".txt")), true);
			FileWriter fw3 = new FileWriter(new File(OUT_FILE_URI.resolve("./graph/"+ "NodeInfo-" +".txt")), true);
			PrintWriter pw2 = new PrintWriter(new BufferedWriter(fw2));
			PrintWriter pw3 = new PrintWriter(new BufferedWriter(fw3));
			//if(this.getRegion()==3) this.region=4;
			pw2.println("timestamp:"+getCurrentTime()+", node-id:"+this.getNodeID()+", block-id:"+newBlock.getId()+", node-region-id:"+this.getRegion()+", block-H:"+newBlock.getHeight());
			pw3.println(getCurrentTime()+","+this.getNodeID()+","+newBlock.getHeight()+","+this.getRegion());
			pw2.close();
			pw3.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		OUT_JSON_FILE.print(		"\"block-id\":" + newBlock.getId());
		OUT_JSON_FILE.print(	"}");
		OUT_JSON_FILE.print("},");
		OUT_JSON_FILE.flush();
	}

	public void addOrphans(Block orphanBlock, Block validBlock){   // Join the orphan block
		if(orphanBlock != validBlock){
			this.orphans.add(orphanBlock);
			this.orphans.remove(validBlock);
			if(validBlock == null || orphanBlock.getHeight() > validBlock.getHeight()){
				this.addOrphans(orphanBlock.getParent(),validBlock);
			}else if(orphanBlock.getHeight() == validBlock.getHeight()){
				this.addOrphans(orphanBlock.getParent(),validBlock.getParent());
			}else{
				this.addOrphans(orphanBlock,validBlock.getParent());
			}
			//新加的尝试
//			if(orphans.contains(orphanBlock.getParent())){
//				orphanBlock.height = orphans.size();
//				orphans.add(orphanBlock);
//			}
//			if(orphans.contains(validBlock.getParent())){
//				validBlock.height = orphans.size();
//				orphans.add(validBlock);
//			}

		}


	}

	public void minting(){
		AbstractMintingTask task = this.consensusAlgo.minting();
		this.mintingTask = task;
		//if (region == 3) removeTask(task);
		if (task != null) putTask(task);
	}

	public void sendInv(Block block){   // Sending information in the block
		for(Node to : this.routingTable.getNeighbors()){
			AbstractMessageTask task = new InvMessageTask(this,to,block);
			//if(region ==3) continue;
			putTask(task);
		}
	}

	public void receiveBlock(Block block){  // Receiving information in the block
		if(this.consensusAlgo.isReceivedBlockValid(block, this.block)){
			if (this.block != null && !this.block.isOnSameChainAs(block)) {
				this.addOrphans(this.block, block);
			}
			this.addToChain(block);
			this.minting();
			this.sendInv(block);
		}else if(!this.orphans.contains(block) && !block.isOnSameChainAs(this.block)){
			this.addOrphans(block, this.block);
			arriveBlock(block, this);
		}
	}

	public void receiveMessage(AbstractMessageTask message){
		Node from = message.getFrom();

		if(message instanceof InvMessageTask){
			Block block = ((InvMessageTask) message).getBlock();
			if(!this.orphans.contains(block) && !this.downloadingBlocks.contains(block)){
				if(this.consensusAlgo.isReceivedBlockValid(block, this.block)){
					AbstractMessageTask task = new RecMessageTask(this,from,block);
					putTask(task);
					downloadingBlocks.add(block);
				}else if(!block.isOnSameChainAs(this.block)){
					// get new orphan block
					AbstractMessageTask task = new RecMessageTask(this,from,block);
					putTask(task);
					downloadingBlocks.add(block);
				}
			}
		}

		if(message instanceof RecMessageTask){
			this.messageQue.add((RecMessageTask) message);
			if(!sendingBlock){
				this.sendNextBlockMessage();
			}
		}

		if(message instanceof BlockMessageTask){
			Block block = ((BlockMessageTask) message).getBlock();
			downloadingBlocks.remove(block);
			this.receiveBlock(block);
		}
	}

	// send a block to the sender of the next queued recMessage
	public void sendNextBlockMessage(){
		if(this.messageQue.size() > 0){

			sendingBlock = true;
			//if(region ==3) sendingBlock = false;

			Node to = this.messageQue.get(0).getFrom();
			Block block = this.messageQue.get(0).getBlock();
			this.messageQue.remove(0);
			long blockSize = BLOCKSIZE;
			long bandwidth = getBandwidth(this.getRegion(),to.getRegion());
			long delay = blockSize * 6 / (bandwidth/1000) + processingTime;
			if(nodeID == 20) delay = 20*60;  //尝试对这个node的delay改变影响block
			BlockMessageTask messageTask = new BlockMessageTask(this, to, block, delay);

			//if(region ==3) removeTask(messageTask);
			putTask(messageTask);
		}else{
			sendingBlock = false;
		}
	}
}
