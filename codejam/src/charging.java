import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class charging {
	static int min_output = 999999;
	static boolean out_yes = false;

	static void check_xor(Integer[] a, Integer[] input) {
		Integer final_array[] = new Integer[a.length];
		for (int k = 0; k < input.length; k++) {

			final_array[k] = Integer.parseInt(a[k].toString(), 2)
					^ Integer.parseInt(input[k].toString(), 2);

		}
		boolean t = true;
		for (int l = 0; l < a.length; l++) {
			// System.out.print(final_array[l]);
			if (final_array[0] != final_array[l])
				t = false;
		}
		int out;
		if (t) {
			out = Integer.parseInt(final_array[0].toString(), 10);

			if (out < min_output) {
				min_output = out;
				out_yes = true;
			}
		}

	}

	static void permute(Integer[] a, int k, Integer[] input) {
		if (k == a.length)
			check_xor(a, input);
		else
			for (int i = k; i < a.length; i++) {
				int temp = a[k];
				a[k] = a[i];
				a[i] = temp;
				permute(a, k + 1, input);
				temp = a[k];
				a[k] = a[i];
				a[i] = temp;
			}
	}

	public static void main(String args[]) {

		try {
			File file = new File("output.out");
			if (!file.exists()) {
				file.createNewFile();
			}
			String content = "";
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			FileInputStream fstream = new FileInputStream(
					"A-small-practiceA.in");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int T, R = 0;

			T = Integer.parseInt(br.readLine());

			for (int i = 1; i <= T; i++) {
				String arr[] = br.readLine().split(" ");
				int N = Integer.parseInt(arr[0]);
				int L = Integer.parseInt(arr[1]);
				String inputs[] = br.readLine().split(" ");
				String outputs[] = br.readLine().split(" ");
				Integer input[] = new Integer[inputs.length];
				Integer output[] = new Integer[outputs.length];
				for (int j = 0; j < inputs.length; j++) {
					input[j] = Integer.parseInt(inputs[j]);
					output[j] = Integer.parseInt(outputs[j]);
				}
				int length = inputs.length;

				permute(output, 0, input);
				if (out_yes) {
					content = "Case #" + i + ": " + min_output + "\n";
				} else {
					content = "Case #" + i + ": " + "NOT POSSIBLE" + "\n";
				}
				out_yes = false;
				min_output = 99999;
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
