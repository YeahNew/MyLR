package util;

import l2r4sr2016.iotools.writeFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 *读取svmrank的pre结果 
 * @author hzy
 */

public class readRankResultForVote {
	static double scoreLow=-1000.0;
	static int top=1000;
	public static void main(String[] args) throws IOException {

		for (scoreLow= 0.9; scoreLow < 1.5; scoreLow+=0.01) {
			//for (int to = 1; to < 20; to++) {
				readEval();
			//}
		}
	}
	public static void readEval() throws IOException {

		String base = "normal20";//willams_10vote normal20
		String testf = "/home/hao/桌面/vote_ranksvm/test/" + base + "/";
		//String pref = "/home/hao/桌面/vote_ranksvm/pre/"+base+"/"+svmc+"/";
		String prefa = "/home/hao/桌面/vote_ranksvm/pre/" + base + "/";

		File prefs[] = new File(prefa).listFiles();
		for (File preff : prefs) {
			String svmc = preff.getName();
			String pref = "/home/hao/桌面/vote_ranksvm/pre/" + base + "/" + svmc + "/";

			File testfs[] = new File(testf).listFiles();
			for (int i = 0; i < testfs.length; i++) {
				//String longidpath="/home/hao/桌面/vote_ranksvm/longid/willams_10vote/";
				String longidpath = "/home/hao/桌面/vote_ranksvm/longid/" + base + "/svmc" + svmc + "_top" + top + "_score" + scoreLow + "/";
	//
				new File(longidpath).mkdirs();
				//System.out.println(i);
	//			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
	//					longidpath+testfs[i].getName())));
	//			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
	//					longidpath+prefs[i].getName())));
				BufferedReader prebr = new BufferedReader(new FileReader(new File(pref + testfs[i].getName())));
				BufferedReader br = new BufferedReader(new FileReader(testfs[i]));
				HashSet<String> reset = new HashSet<String>();
				String qidtemp = "qid:-1";
				String line = null;
				ArrayList<Double> prelist = new ArrayList<>();
				ArrayList<String> testlist = new ArrayList<>();
				int tt = 0;
				while ((line = br.readLine()) != null) {
					tt++;
					line += " ";
					String qid = parserQid(line);
					if (qidtemp.equals(qid)) {
						qidtemp = qid;
						String stttttt = prebr.readLine();
	//					System.out.println(tt);
	//					System.out.println(stttttt);
						prelist.add(Double.parseDouble(stttttt));
						//	System.out.println(line);
						testlist.add(line.split("#")[1]);

					} else if (!qidtemp.equals(qid)) {
						qidtemp = qid;

						ArrayList<Integer> rerankList = reRank(prelist);

						for (int j = 0; j < Math.min(top, rerankList.size()); j++) {
							if (rerankList.get(j) == -1) {

							} else {
								String longids = testlist.get(rerankList.get(j));
								if (longids.length() > 1) {
									longids = longids.split(" ")[0];
									reset.add(longids);
									//bw.write(longids);
									//bw.flush();
									//reset
								}
							}
						}

						testlist.clear();
						prelist.clear();
						prelist.add(Double.parseDouble(prebr.readLine()));
						testlist.add(line.split("#")[1]);
					}

				}
				ArrayList<Integer> rerankList = reRank(prelist);
				for (int j = 0; j < Math.min(top, rerankList.size()); j++) {

					if (rerankList.get(j) == -1) {

					} else {
						String longids = testlist.get(rerankList.get(j));
						if (longids.length() > 1) {
							longids = longids.split(" ")[0];
							reset.add(longids);
							//bw.write(longids);
							//bw.flush();
							//reset
						}
					}
				}
				//bw.close();
				br.close();
				prebr.close();
				writeFile.writeResult(reset, longidpath + testfs[i].getName());
			}
		}
	}
	
	public static String parserQid(String line){
		return line.split(" ")[1];
	}
	
	public static ArrayList<Integer> reRank(ArrayList<Double> preList){
		TreeMap<Integer, Double> tm = new TreeMap<Integer, Double>();
		
		ArrayList<Integer> re = new ArrayList<Integer>();
		int preSize = preList.size();
		for (int i = 0; i < preSize; i++) {
			//double onePre = preList.get(i);
			//tm.put(i, onePre);
			double onePre = preList.get(i);
			if(onePre>scoreLow){
				tm.put(i, onePre);
			}else{
				tm.put(-1, onePre);
			}
		}
		
		List<Entry<Integer, Double>> al = new ArrayList(tm.entrySet()); 
		Collections.sort(al, new Comparator(){
			public int compare(Object o1, Object o2){
				Entry obj1 = (Entry) o1;
	        	Entry obj2 = (Entry) o2;
	          	return ((Double) obj2.getValue()).compareTo((Double)obj1.getValue());      
			}
		});

		for (Entry<Integer, Double> i : al) {
			re.add(i.getKey());
		}
		return re;
	}
	
}