import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Arrays;

public class war_2014 {
	public static void main(String args[]) {
		try {
			File file = new File("output.out");
			if (!file.exists()) {
				file.createNewFile();
			}
			String content = "";
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			FileInputStream fstream = new FileInputStream("D-large.in");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int T, R = 0;

			T = Integer.parseInt(br.readLine());

			for (int i = 1; i <= T; i++) {
				int N = Integer.parseInt(br.readLine());
				String st1[] = new String[N];
				String st2[] = new String[N];
				st1 = br.readLine().split(" ");
				st2 = br.readLine().split(" ");
				double naomi[] = new double[N];
				double ken[] = new double[N];
				for (int j = 0; j < N; j++) {
					naomi[j] = Double.parseDouble(st1[j]);
					ken[j] = Double.parseDouble(st2[j]);
				}
				Arrays.sort(naomi);
				Arrays.sort(ken);
				{
					int ken_score = 0, naomi_score = 0;

					// Figuring out the deceitful method score
					double naomi_temp[] = naomi.clone();
					double ken_temp[] = ken.clone();
					for (int j = 0; j < N; j++) {
						if (naomi_temp[0] < ken_temp[0]) {
							ken_score++;
							double naomi_temp1[] = new double[(naomi_temp.length) - 1];
							double ken_temp1[] = new double[(ken_temp.length) - 1];
							for (int k = 0; k < naomi_temp.length - 1; k++) {
								naomi_temp1[k] = naomi_temp[k + 1];
							}
							naomi_temp = naomi_temp1;
							for (int k = 0; k < ken_temp.length - 1; k++) {
								ken_temp1[k] = ken_temp[k];
							}
							ken_temp = ken_temp1;
						} else if (naomi_temp[0] > ken_temp[0]) {
							naomi_score++;
							double naomi_temp1[] = new double[(naomi_temp.length) - 1];
							double ken_temp1[] = new double[(ken_temp.length) - 1];
							for (int k = 0; k < naomi_temp.length - 1; k++) {
								naomi_temp1[k] = naomi_temp[k + 1];
							}
							naomi_temp = naomi_temp1;
							for (int k = 0; k < ken_temp.length - 1; k++) {
								ken_temp1[k] = ken_temp[k + 1];
							}
							ken_temp = ken_temp1;
						}
					}

					content = "Case #" + i + ": " + naomi_score;
				}
				{
					int ken_score = 0, naomi_score = 0;

					// Figuring out the deceitful method score
					double naomi_temp[] = naomi.clone();
					double ken_temp[] = ken.clone();
					for (int j = 0; j < naomi_temp.length; j++) {
						for (int k = 0; k < ken_temp.length; k++) {
							if (naomi_temp[j] < ken_temp[k]) {
								double ken_temp1[] = new double[(ken_temp.length)
										- k - 1];
								for (int l = 0; l < ken_temp1.length; l++) {
									ken_temp1[l] = ken_temp[k + 1];
									k++;
								}
								ken_temp = ken_temp1;
								ken_score++;
								break;
							}
						}
					}

					content = content + " " + (N - ken_score) + "\n";
				}
				bw.write(content);
				System.out.print(content);

			}

			// }
			in.close();
			bw.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}
