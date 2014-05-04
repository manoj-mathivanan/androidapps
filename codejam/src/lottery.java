import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class lottery {
	public static void main(String args[]) {
		try {
			File file = new File("output.out");
			if (!file.exists()) {
				file.createNewFile();
			}
			String content = "";
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			FileInputStream fstream = new FileInputStream("B-large (1).in");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int N, R = 0;

			N = Integer.parseInt(br.readLine());

			for (int i = 1; i <= N; i++) {

				String st1[] = new String[3];
				st1 = br.readLine().split(" ");
				long A = 0, B = 0, K = 0;

				A = Long.parseLong(st1[0]);
				B = Long.parseLong(st1[1]);
				K = Long.parseLong(st1[2]);

				long total = 0;
				if (K > A && K > B) {
					total = A * B;
				} else {
					long min = A;
					if (min > B)
						min = B;
					if (min > K)
						min = K;
					K = min;
					total = K * (B) + K * (A - K);
					for (long j = K; j < A; j++) {
						for (long l = K; l < B; l++) {
							long ans = j & l;
							if (ans < K)
								total++;
						}
					}
				}

				content = "Case #" + i + ": " + total + "\n";
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
