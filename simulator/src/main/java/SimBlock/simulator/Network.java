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

import static SimBlock.settings.NetworkConfiguration.*;
import static SimBlock.simulator.Main.*;
import SimBlock.block.Block;

import java.util.List;

public class Network {   // Configure network parameters
	//"NORTH_AMERICA", "EUROPE", "SOUTH_AMERICA", "ASIA_PACIFIC", "JAPAN", "AUSTRALIA"

	// latency according with 20% variance pallet distribution
	public static final long getLatency(int from, int to){
		long mean = LATENCY[from][to];
		double shape = 0.1 * mean;
		double scale = mean - 10;  // Calculate the corresponding latency according to the parameters in the configuration class
//		if(PARTITION_REGION ==0 && from==0 && to == 0) return 100;
//		if(PARTITION_REGION ==0 && from==0 && to == 0) return 100;
//		if(PARTITION_REGION ==1 && from==1 && to == 1) return 100;
//		if(PARTITION_REGION ==1 && from==1 && to == 1) return 100;
//		if(PARTITION_REGION ==2 && from==2 && to == 2) return 100;
//		if(PARTITION_REGION ==2 && from==2 && to == 2) return 100;
//		if(PARTITION_REGION ==3 && from==3 && to == 3) return 100;
//		if(PARTITION_REGION ==3 && from==3 && to == 3) return 100;
//		if(PARTITION_REGION ==4 && from==4 && to == 4) return 100;
//		if(PARTITION_REGION ==4 && from==4 && to == 4) return 100;
//		if(PARTITION_REGION ==5 && from==5 && to == 5) return 100;
//		if(PARTITION_REGION ==5 && from==5 && to == 5) return 100;
		if(PARTITION_REGION ==0 && from==0 && to!=0) return 999999;  // Affect the propagation between regions by changing the value of Latency
		if(PARTITION_REGION ==0 && to==0 && from!=0) return 999999;
		if(PARTITION_REGION ==1 && from==1 && to!=1) return 999999;
		if(PARTITION_REGION ==1 && to==1 && from!=1) return 999999;
		if(PARTITION_REGION ==2 && from==2 && to !=2) return 999999;
		if(PARTITION_REGION ==2 && to==2 && from !=2) return 999999;
		if(PARTITION_REGION ==3 && from==3 && to !=3) return 999999;
		if(PARTITION_REGION ==3 && to==3 && from !=3) return 999999;
		if(PARTITION_REGION ==4 && from==4 && to !=4) return 999999;
		if(PARTITION_REGION ==4 && to==4 && from !=4) return 999999;
		if(PARTITION_REGION ==5 && from==5 && to !=5) return 999999;
		if(PARTITION_REGION ==5 && to==5 && from !=5) return 999999;


		return Math.round( scale / Math.pow(random.nextDouble(),1.0/shape) );
	}

	// bandwidth
	public static final long getBandwidth(int from, int to) {  // The propagation bandwidth of the region
		//if(from == 1) return 9;
		return Math.min(UPLOAD_BANDWIDTH[from], DOWNLOAD_BANDWIDTH[to]);
	}

	public static List<String> getRegionList() {
		return REGION_LIST;
	}

	public static double[] getRegionDistribution() {
		return REGION_DISTRIBUTION;
	}   // Get region distribution parameters

	public static double[] getDegreeDistribution() {    // Get degree distribution parameters
		return DEGREE_DISTRIBUTION;
	}

	public static void printRegion(){  // Write region static parameters
		STATIC_JSON_FILE.print("{\"region\":[");

		int id = 0;
		for(; id < REGION_LIST.size() -1; id++){
			STATIC_JSON_FILE.print("{");
			STATIC_JSON_FILE.print(		"\"id\":" + id + ",");
			STATIC_JSON_FILE.print(		"\"name\":\"" + REGION_LIST.get(id) + "\"");
			STATIC_JSON_FILE.print("},");
		}

		STATIC_JSON_FILE.print(	   "{");
		STATIC_JSON_FILE.print(			"\"id\":" + id + ",");
		STATIC_JSON_FILE.print(			"\"name\":\"" + REGION_LIST.get(id) + "\"");
		STATIC_JSON_FILE.print(    "}");
		STATIC_JSON_FILE.print("]}");
		STATIC_JSON_FILE.flush();
		STATIC_JSON_FILE.close();
	}
}
