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
package SimBlock.task;

import static SimBlock.simulator.Main.*;
import static SimBlock.simulator.Network.*;
import static SimBlock.simulator.Timer.*;

import SimBlock.block.Block;
import SimBlock.node.Node;

import java.io.*;

public class BlockMessageTask extends AbstractMessageTask {   // Processing of information in the block

	private Block block;
	private long interval;

	public BlockMessageTask(Node from, Node to, Block block ,long delay) {
		super(from, to);
		this.block = block;
		this.interval = getLatency(this.getFrom().getRegion(), this.getTo().getRegion()) + delay;
	}

	public long getInterval(){
		return this.interval;
	}

	public void run(){

		this.getFrom().sendNextBlockMessage();
		
		OUT_JSON_FILE.print("{");
		OUT_JSON_FILE.print(	"\"kind\":\"flow-block\",");
		OUT_JSON_FILE.print(	"\"content\":{");
		OUT_JSON_FILE.print(		"\"transmission-timestamp\":" + (getCurrentTime() - this.interval) + ",");
		OUT_JSON_FILE.print(		"\"reception-timestamp\":" + getCurrentTime() + ",");
		OUT_JSON_FILE.print(		"\"begin-node-id\":" + getFrom().getNodeID() + ",");
		OUT_JSON_FILE.print(		"\"end-node-id\":" + getTo().getNodeID() + ",");
		OUT_JSON_FILE.print(		"\"block-id\":" + block.getId());
		OUT_JSON_FILE.print(	"}");
		OUT_JSON_FILE.print("},");
		OUT_JSON_FILE.flush();
		try {
			FileWriter fw3 = new FileWriter(new File(OUT_FILE_URI.resolve("./graph/"+ "Propagation" +".txt")), true);
			PrintWriter pw3 = new PrintWriter(new BufferedWriter(fw3));
			pw3.println("transmission-timestamp:" + (getCurrentTime() - this.interval) + ", reception-timestamp:" + getCurrentTime() + ", begin-node-id:" + getFrom().getNodeID() + ", end-node-id:" + getTo().getNodeID() + ", block-id:" + block.getId());
			pw3.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		super.run();
	}

	public Block getBlock(){
		return this.block;
	}


}
