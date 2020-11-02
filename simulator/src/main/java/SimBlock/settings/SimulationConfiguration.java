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
package SimBlock.settings;

public class SimulationConfiguration {
    // Simulation configuration parameter initialization of the blockchain network program

	// The number of nodes participating in the blockchain network.
	public static  int NUM_OF_NODES = 100;//600;//800;//6000;


	public static final String TABLE = "SimBlock.node.routingTable.BitcoinCoreTable";
	public static final String ALGO = "SimBlock.node.consensusAlgo.SampleProofOfStake";

	// The expected value of block generation interval. The difficulty of mining is automatically adjusted by this value and the sum of mining power.
	// (unit: millisecond)
	public static  long INTERVAL = 1000*60;//1000*60;//1000*30*5;//1000*60*10;

	// Mining power is the number of mining (hash calculation) executed per millisecond.
	public static  int AVERAGE_MINING_POWER = 500;  // The average mining power of each node.
	public static  int STDEV_OF_MINING_POWER = 10;   // Mining power corresponds to Hash Rate in Bitcoin, and is the number of mining (hash calculation) executed per millisecond.
	// The mining power of each node is determined randomly according to the normal distribution whose average is AVERAGE_MINING_POWER and standard deviation is STDEV_OF_MINING_POWER.

	//STDEV: stardard deviation 标准差

	public static  int AVERAGE_COINS = 200;
	public static  int STDEV_OF_COINS = 100;


	public static  double STAKING_REWARD = 0.01;

	// The block height when a simulation ends.
	public static  int ENDBLOCKHEIGHT = 20;

	// Unit: byte  Block size. (unit: byte)
	public static  long BLOCKSIZE = 8000;//6110;//8000


}
