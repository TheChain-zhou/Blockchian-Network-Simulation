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
package SimBlock.simulator;

import static SimBlock.settings.NetworkConfiguration.PARTITION_REGION;
import static SimBlock.settings.SimulationConfiguration.*;
import static SimBlock.simulator.Network.*;
import static SimBlock.simulator.Simulator.*;
import static SimBlock.simulator.Timer.*;
import static SimBlock.node.Node.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;



import SimBlock.block.Block;
import SimBlock.node.Node;
import SimBlock.swing.WinFrame;
import SimBlock.task.AbstractMintingTask;

public class Main {
	public static Random random = new Random(10);
	public static long time1 = 0;//a value to know the simulation time.

	public static URI CONF_FILE_URI;
	public static URI OUT_FILE_URI;
	static {
		try {
			CONF_FILE_URI = ClassLoader.getSystemResource("simulator.conf").toURI();
			OUT_FILE_URI = CONF_FILE_URI.resolve(new URI("../output/"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static PrintWriter OUT_JSON_FILE;
	public static PrintWriter STATIC_JSON_FILE;
	static {
		try{
			OUT_JSON_FILE = new PrintWriter(new BufferedWriter(new FileWriter(new File(OUT_FILE_URI.resolve("./output.json")))));
			STATIC_JSON_FILE = new PrintWriter(new BufferedWriter(new FileWriter(new File(OUT_FILE_URI.resolve("./static.json")))));
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public static String Click(){  //simulation starts
		long start = System.currentTimeMillis();   //the start time
		setTargetInterval(INTERVAL);   //block generation interval

		OUT_JSON_FILE.print("["); //start json format
		OUT_JSON_FILE.flush();

		printRegion(); // set the region info into static file

		constructNetworkWithAllNode(NUM_OF_NODES);  //node and network construction

		int j=1;
		while(getTask() != null){

			if(getTask() instanceof AbstractMintingTask){
				AbstractMintingTask task = (AbstractMintingTask) getTask();
				if(task.getParent().getHeight() == j) j++;
				if(j > ENDBLOCKHEIGHT){break;}
				if(j%100==0 || j==2) writeGraph(j);
			}
			runTask();
		}


		printAllPropagation();   //get propagation info

		System.out.println();

		Set<Block> blocks = new HashSet<Block>();
		//Set<Block> block0 = new HashSet<Block>();

		// 尝试开始
		for(int i=0; i<NUM_OF_NODES; i++) {  //尝试一下
			Set<Block> blockx = new HashSet<Block>();
			Block block = null;
			//System.out.println(block);
			block = getSimulatedNodes().get(i).getBlock();   //block generation
			while(block!= null){
				blockx.add(block);
				block = block.getParent();
			}

			Set<Block> orphans = new HashSet<Block>();

			int averageOrhansSize =0;
			for(Node node :getSimulatedNodes()){   //orphan block generation
				orphans.addAll(node.getOrphans());
				averageOrhansSize += node.getOrphans().size();
			}
			averageOrhansSize = averageOrhansSize/getSimulatedNodes().size();
			blockx.addAll(orphans);

			ArrayList<Block> blockList = new ArrayList<Block>();
			blockList.addAll(blockx);
			//System.out.println(blockList);

			Collections.sort(blockList, new Comparator<Block>(){
				@Override
				public int compare(Block a, Block b){
					int order = Long.signum(a.getTime() - b.getTime());
					if(order != 0) return order;
					order = System.identityHashCode(a) - System.identityHashCode(b);
					return order;
				}
			});

			try {
				String string1 = String.format("./blockList#%d.txt", i+1);
				String string2 = String.valueOf(string1);
				//String string = String.format("./blockList %s.txt", i);
				FileWriter fw = new FileWriter(new File(string2), false);
				PrintWriter pw = new PrintWriter(new BufferedWriter(fw));   // Table for the blockList

				for(Block b:blockList){
					long time000 =  System.currentTimeMillis()-start;
					if(!orphans.contains(b)){
						pw.println("OnChain : "+b.getHeight()+" : "+b);   //blocks which are on the chain
					}
					else{
						pw.println("Orphan : "+b.getHeight()+" : "+b);    //block which are not on the chain
						if(orphans.contains(b.getParent()))  pw.println("the parent of this orphan is also an orphan, and connect them together: " + b.getParent()+ ", "+ b);
					}
				}
				pw.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}


		}    //尝试结束位置

		Block block = null;
		block  = getSimulatedNodes().get(0).getBlock();   //block generation

		while(block != null){
			blocks.add(block);
			block = block.getParent();
		}

		Set<Block> orphans = new HashSet<Block>();

		int averageOrhansSize =0;
		for(Node node :getSimulatedNodes()){   //orphan block generation
			orphans.addAll(node.getOrphans());
			averageOrhansSize += node.getOrphans().size();
		}
		averageOrhansSize = averageOrhansSize/getSimulatedNodes().size();



		blocks.addAll(orphans);

		ArrayList<Block> blockList = new ArrayList<Block>();
		blockList.addAll(blocks);

		//ArrayList<Block> OrphanList = new ArrayList<Block>();
		//OrphanList.addAll(orphans);

		Collections.sort(blockList, new Comparator<Block>(){
			@Override
			public int compare(Block a, Block b){
				int order = Long.signum(a.getTime() - b.getTime());
				if(order != 0) return order;
				order = System.identityHashCode(a) - System.identityHashCode(b);
				return order;
			}
		});
		for(Block orphan : orphans){
			System.out.println(orphan+ ":" +orphan.getHeight());
		}
		System.out.println(averageOrhansSize);

		try {
			FileWriter fw = new FileWriter(new File(OUT_FILE_URI.resolve("./blockList.txt")), false);
			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));   // Table for the blockList

			for(Block b:blockList){
				long time000 =  System.currentTimeMillis()-start;
				if(!orphans.contains(b)){
					pw.println("OnChain : "+b.getHeight()+" : "+b);   //blocks which are on the chain
				}else{
					pw.println("Orphan : "+b.getHeight()+" : "+b);    //block which are not on the chain
					if(orphans.contains(b.getParent()))  pw.println("the parent of this orphan is also an orphan, and connect them together: " + b.getParent()+ ", "+ b);
				}
			}
			pw.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		OUT_JSON_FILE.print("{");
		OUT_JSON_FILE.print(	"\"kind\":\"simulation-end\",");   //print the time for the end of the simulation into output
		OUT_JSON_FILE.print(	"\"content\":{");
		OUT_JSON_FILE.print(		"\"timestamp\":" + getCurrentTime());
		OUT_JSON_FILE.print(	"}");
		OUT_JSON_FILE.print("}");
		OUT_JSON_FILE.print("]"); //end json format
		OUT_JSON_FILE.close();
		long end = System.currentTimeMillis();   //print end time
		time1 += end -start;
		System.out.println(time1);



		return "The parameter setting is successful, the operation is completed, and the output result is in the output folder.";
	}

	public static void main(String[] args){
		WinFrame frame = new WinFrame("Configuration parameter input page");   //start the Configuration step

	}


	//TODO　以下初始生成将加载方案
	//创建一个任务来加入节点（将节点加入和开始链接的任务分开）
	//在方案文件中，将上述参与任务插入Timer中

	//TODO　The following initial generation will load the scheme
    //Create a task to join the node (separate the task of joining the node and starting the link)
    //In the program file, insert the above participating tasks into the Timer

	public static ArrayList<Integer> makeRandomList(double[] distribution ,boolean facum){
		ArrayList<Integer> list = new ArrayList<Integer>();    // the list array
		int index=0;

		if(facum){
			for(; index < distribution.length ; index++){
				while(list.size() <= NUM_OF_NODES * distribution[index]){
					list.add(index);
				}
			}
			while(list.size() < NUM_OF_NODES){
				list.add(index);
			}
		}else{
			double acumulative = 0.0;
			for(; index < distribution.length ; index++){
				acumulative += distribution[index];
				while(list.size() <= NUM_OF_NODES * acumulative){
					list.add(index);
				}
			}
			while(list.size() < NUM_OF_NODES){
				list.add(index);
			}
		}

		Collections.shuffle(list, random);
		return list;
	}

	public static int genMiningPower(){   // the function for generate mining power
		double r = random.nextGaussian();

		return  Math.max((int)(r * STDEV_OF_MINING_POWER + AVERAGE_MINING_POWER),1);
	}
	public static void constructNetworkWithAllNode(int numNodes){
		List<Integer> main_B=new ArrayList<Integer>();
		List<Integer> other_B=new ArrayList<Integer>();
		//List<String> regions = new ArrayList<>(Arrays.asList("NORTH_AMERICA", "EUROPE", "SOUTH_AMERICA", "ASIA_PACIFIC", "JAPAN", "AUSTRALIA", "OTHER"));
		double[] regionDistribution = getRegionDistribution();  //Read the configuration parameters of the region distribution
		List<Integer> regionList  = makeRandomList(regionDistribution,false);
		double[] degreeDistribution = getDegreeDistribution();   //Read configuration parameters of degree distribution
		List<Integer> degreeList  = makeRandomList(degreeDistribution,true);
		
		for(int id = 1; id <= numNodes; id++){   // Configure node info into output file
			Node node = new Node(id,degreeList.get(id-1)+1,regionList.get(id-1), genMiningPower(),TABLE,ALGO);
			addNode(node);

			OUT_JSON_FILE.print("{");
			OUT_JSON_FILE.print(	"\"kind\":\"add-node\",");
			OUT_JSON_FILE.print(	"\"content\":{");
			OUT_JSON_FILE.print(		"\"timestamp\":0,");
			OUT_JSON_FILE.print(		"\"node-id\":" + id + ",");
			OUT_JSON_FILE.print(		"\"region-id\":" + regionList.get(id-1));
			OUT_JSON_FILE.print(	"}");
			OUT_JSON_FILE.print("},");
			OUT_JSON_FILE.flush();

		}

		for(Node node: getSimulatedNodes()) {
			if(node.getRegion() != PARTITION_REGION){   // here we put nodeId=0 out of the grouped node list
				main_B.add(node.getNodeID());
				//System.out.println(main_B);
				node.joinNetwork();      //  Let the anti-partitioning node join the network to participate in the simulation
			}
		}
		getSimulatedNodes().get(main_B.get(0)).genesisBlock(); // Start building the Genesis block

		for(Node node: getSimulatedNodes()) {
			if (node.getRegion() == PARTITION_REGION) {
				other_B.add(node.getNodeID());
				node.miningPower = 5000;
				//System.out.println(other_B);
				//node.processingTime = 10000;
				//node.miningPower = 100;
				node.joinNetwork();     // form the new network for these nodes in the the partition region.
			}
		}
		//System.out.println(other_B.size());
		getSimulatedNodes().get(other_B.get(0)).genesisBlock();
		//getSimulatedNodes().get(0).genesisBlock();

	}

//	for(Node node: getSimulatedNodes()) { // 原本的加入network方法
//		node.joinNetwork();      //  Let the anti-partitioning node join the network to participate in the simulation
//
//	}
//	getSimulatedNodes().genesisBlock(); // Start building the Genesis block

	public static void writeGraph(int j){
		try {
			FileWriter fw = new FileWriter(new File(OUT_FILE_URI.resolve("./graph/"+ j +".txt")), false);
			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));   // Write graph output result file

            for(int index =1;index<=getSimulatedNodes().size();index++){
    			Node node = getSimulatedNodes().get(index-1);
    			for(int i=0;i<node.getNeighbors().size();i++){
					//if(node.getNodeID()==2) continue;          //shield node routing function
					Node neighter = node.getNeighbors().get(i);
    				//pw.println(node.getNodeID()+"(" +node.getRegion()+") "+neighter.getNodeID()+"("+neighter.getRegion()+")");
					pw.println(node.getNodeID()+" "+neighter.getNodeID());
    			}
            } 
            pw.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}

}
