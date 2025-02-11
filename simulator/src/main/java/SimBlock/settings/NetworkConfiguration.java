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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Setting Network latency and bandwith
public class NetworkConfiguration {
	// Regions where nodes can exist.
	public static List<String> REGION_LIST = new ArrayList<>(Arrays.asList("NORTH_AMERICA", "EUROPE", "SOUTH_AMERICA", "ASIA_PACIFIC", "JAPAN", "AUSTRALIA"));

	public static int PARTITION_REGION = 1;
	// LATENCY[i][j] is average latency from REGION_LIST[i] to REGION_LIST[j]
	// Unit: millisecond
	private static final long[][] LATENCY_2015 = {
		{36, 119, 255, 310, 154, 208},
		{119, 12, 221, 242, 266, 350},
		{255, 221, 137, 347, 256, 269},
		{310, 242, 347, 99, 172, 278},
		{154, 266, 256, 172, 9, 163},
		{208, 350, 269, 278, 163, 22}};
	public static  long[][] LATENCY_2019 = {  // List of latency assigned to each region. (unit: millisecond)
		{132, 124, 184, 198, 151, 189},
		{124, 111, 227, 237, 252, 294},
		{184, 227, 128, 325, 301, 322},
		{198, 237, 325, 185, 118, 198},
		{151, 252, 301, 258, 112, 126},
		{189, 294, 322, 198, 126, 116}};

	public static final long[][] LATENCY = LATENCY_2019;
	

	// Download bandwidth in each region, and last element is Inter-regional bandwidth
	// Unit: bit per second
	private static final long[] DOWNLOAD_BANDWIDTH_2015 = {25000000, 24000000, 6500000, 10000000, 17500000, 14000000, 6 * 1000000};
	private static final long[] DOWNLOAD_BANDWIDTH_2019 = {1000000, 1000000, 2000000, 2000000, 2000000, 3000000, 6 * 1000000};

	// List of download bandwidth assigned to each region. (unit: bit per second)
	public static final long[] DOWNLOAD_BANDWIDTH = DOWNLOAD_BANDWIDTH_2019;

	// Upload bandwidth in each region, and last element is Inter-regional bandwidth
	// Unit: bit per second
	private static final long[] UPLOAD_BANDWIDTH_2015 =  { 4700000,  8100000, 1800000,  5300000,  3400000,  5200000, 6 * 1000000};
	private static final long[] UPLOAD_BANDWIDTH_2019 =  { 1000000,  1000000, 3000000,  2000000,  2000000,  3000000, 6 * 1000000};

	// List of upload bandwidth assigned to each region. (unit: bit per second)
	public static final long[] UPLOAD_BANDWIDTH = UPLOAD_BANDWIDTH_2019;

	// Each value means the rate of the number of nodes in the corresponding region to the number of all nodes.
	// The distribution of node's region. Each value means the rate of the number of nodes in the corresponding region to the number of all nodes.
	private static final double[] REGION_DISTRIBUTION_BITCOIN_2015 = { 0.3869, 0.5159, 0.0113, 0.0574, 0.0119, 0.0166};
	public static  double[] REGION_DISTRIBUTION_BITCOIN_2019 = { 0.3, 0.2, 0.1, 0.1, 0.2, 0.1};
	private static final double[] REGION_DISTRIBUTION_LITECOIN     = { 0.3661, 0.4791, 0.0149, 0.1022, 0.0238, 0.0139};
	private static final double[] REGION_DISTRIBUTION_DOGECOIN     = { 0.3924, 0.4879, 0.0212, 0.0697, 0.0106, 0.0182};

	public static  double[] REGION_DISTRIBUTION = REGION_DISTRIBUTION_BITCOIN_2019;

	// The cumulative distribution of number of outbound links. Cf. Andrew Miller et al., "Discovering bitcoin's public topology and influential nodes", 2015.
	private static final double[] DEGREE_DISTRIBUTION_BITCOIN_2015 = {0.005,0.070,0.075,0.10,0.20,0.30,0.40,0.50,0.60,0.70,0.80,0.85,0.90,0.95,0.97,0.97,0.98,0.99,0.995,1.0};
	private static final double[] DEGREE_DISTRIBUTION_LITECOIN     = {0.01,0.02,0.04,0.07,0.09,0.14,0.20,0.28,0.39,0.5,0.6,0.69,0.76,0.81,0.85,0.87,0.89,0.92,0.93,1.0};
	private static final double[] DEGREE_DISTRIBUTION_DOGECOIN     = {0.00,0.00,0.00,0.00,0.00,0.00,0.00,1.0,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.00,1.0};

	public static final double[] DEGREE_DISTRIBUTION = DEGREE_DISTRIBUTION_BITCOIN_2015;
}