import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class sample1 {
	public static void main(String args[]) {
		try {
			File file = new File("output.out");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			FileInputStream fstream = new FileInputStream("A-large-practice.in");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int N, C, I = 0;

			N = Integer.parseInt(br.readLine());

			for (int i = 1; i <= N; i++) {
				C = Integer.parseInt(br.readLine());
				I = Integer.parseInt(br.readLine());
				String st[] = br.readLine().split(" ");
				Integer P[] = new Integer[I];
				for (int j = 0; j < I; j++) {
					P[j] = Integer.parseInt(st[j]);
				}
				for (int j = 0; j < I; j++) {
					for (int k = j + 1; k < I; k++) {
						if (P[j] + P[k] == C) {
							String content = "Case #" + i + ": " + (j + 1)
									+ " " + (k + 1) + "\n";
							System.out.print(content);
							bw.write(content);

							j = I + 1;
							break;
						}
					}
				}
			}

			// }
			in.close();
			bw.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}
