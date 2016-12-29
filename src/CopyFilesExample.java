import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class CopyFilesExample {

	public static void main(String[] args) throws InterruptedException, IOException {

		File source = new File("C:\\cabis\\before\\test1.txt");
		File dest = new File("C:\\cabis\\after\\test1.txt");

		// copy file using FileStreams
		long start = System.nanoTime();
		long end;
		copyFileUsingFileStreams(source, dest);
		System.out.println("Time taken by FileStreams Copy = " + (System.nanoTime() - start));

		// copy files using java.nio.FileChannel
		source = new File("C:\\cabis\\before\\test2.txt");
		dest = new File("C:\\cabis\\after\\test2.txt");
		start = System.nanoTime();
		copyFileUsingFileChannels(source, dest);
		end = System.nanoTime();
		System.out.println("Time taken by FileChannels Copy = " + (end - start));

		// copy file using Java 7 Files class
		source = new File("C:\\cabis\\before\\test3.txt");
		dest = new File("C:\\cabis\\after\\test3.txt");
		start = System.nanoTime();
		copyFileUsingJava7Files(source, dest);
		end = System.nanoTime();
		System.out.println("Time taken by Java7 Files Copy = " + (end - start));

		// Log 분석해서 INSERT, UPDATE, DELTE 목록 뽑기
		// 이동전의 폴더
		String inFolder = "C:/Users/BLOW/Downloads/tmp1/tmp1/org";
		// 이동후의 폴더
		String outFolder = "C:\\Users\\BLOW\\Desktop";

		// 이동전의 폴더에 있는 파일들을 읽는다.
		List<File> dirList = getDirFileList(inFolder);

		// 폴더의 사이즈만큼 돌면서 파일을 이동시킨다.
		for (int i = 0; i < dirList.size(); i++) {
			// i번째 저장되어 있는 파일을 불러온다.
			String fileName = dirList.get(i).getName();
			System.out.println(fileName);
			// 파일 삭제를 원한다면
			// fileDelete(inFolder+"\\"+fileName);

			// 파일 복사을 원한다면
			// fileCopy(inFolder+"\\"+fileName, outFolder+"\\"+fileName);

			// 파일 이동을 원한다면
			// fileMove(inFolder+"\\"+fileName, outFolder+"\\"+fileName);

			// 파일 생성을 원한다면
			// fileMake("C:/Users/INTERPARK/Desktop/test.txt");
		  }
	}

	private static void copyFileUsingFileStreams(File source, File dest) throws IOException {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(source);
			output = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
		} finally {
			input.close();
			output.close();
		}
	}

	private static void copyFileUsingFileChannels(File source, File dest) throws IOException {
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(dest).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} finally {
			inputChannel.close();
			outputChannel.close();
		}
	}

	private static void copyFileUsingJava7Files(File source, File dest) throws IOException {
		Files.copy(source.toPath(), dest.toPath());
	}
	
	/**
	 * <p>메소드명		: chkFile</p>
	 * <p>설명			: 폴더생성</p>
	 * <p>메소드인수1 	: String path</p>
	 * <p>메소드리턴값	: N/A</p>
	 * <p>예외처리		: Exception</p>
	 */
	public static void mkDir(String path) throws Exception {
		File file = new File(path);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
	}

	// 디렉토리의 파일 리스트를 읽는 메소드
	public static List<File> getDirFileList(String dirPath) {
		// 디렉토리 파일 리스트
		List<File> dirFileList = null;

		// 파일 목록을 요청한 디렉토리를 가지고 파일 객체를 생성함
		File dir = new File(dirPath);

		// 디렉토리가 존재한다면
		if (dir.exists()) {
			// 파일 목록을 구함
			File[] files = dir.listFiles();

			// 파일 배열을 파일 리스트로 변화함
			dirFileList = Arrays.asList(files);
		}

		return dirFileList;
	}
}
