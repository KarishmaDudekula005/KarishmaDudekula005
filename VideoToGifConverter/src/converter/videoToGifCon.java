package converter;

	import java.io.File;
	import java.io.IOException;

	public class videoToGifCon {

	    public static void main(String[] args) {
	        String videoUrl = "https://youtube.com/shorts/uJhmpwBT68I?si=supPELiPZpF_tRS2"; // Example URL
	        String downloadPath = "tools/downloaded_video"; // Path for the downloaded video
	        
	        try {
	            // Step 1: Download the video using yt-dlp
	            downloadVideo(videoUrl, downloadPath);
	            
	            // Step 2: Find the downloaded video file
	            String videoFilePath = findDownloadedFile(downloadPath);
	            if (videoFilePath == null) {
	                System.out.println("Video download failed.");
	                return;
	            }
	            System.out.println("Downloaded file path: " + videoFilePath);
	            
	            // Step 3: Convert video to GIFs
	            convertToGifs(videoFilePath);
	        } catch (IOException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }

	    public static void downloadVideo(String videoUrl, String downloadPath) throws IOException {
	        ProcessBuilder pb = new ProcessBuilder(
	            "tools/yt-dlp.exe", 
	            videoUrl, 
	            "-o", downloadPath + ".%(ext)s" // Automatically use the correct extension
	        );
	        Process process = pb.start();
	        try {
	            process.waitFor();  // Wait until the download is complete
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        System.out.println("Video downloaded successfully!");
	    }

	    public static String findDownloadedFile(String downloadBasePath) {
	        File toolsFolder = new File("tools");
	        for (File file : toolsFolder.listFiles()) {
	            if (file.getName().startsWith("downloaded_video")) {
	                return file.getPath(); // Return the full path of the downloaded video file
	            }
	        }
	        return null; // Return null if no file is found
	    }

	    public static void convertToGifs(String videoFilePath) {
	        String ffmpegPath = "tools/ffmpeg.exe"; // Path to ffmpeg
	        int gifDuration = 10; // Duration of each GIF (in seconds)
	        int startTime = 0; // Start time for GIF creation
	        
	        while (startTime < getVideoDuration(videoFilePath)) {
	            String outputGifPath = "tools/output_" + startTime + ".gif";
	            ProcessBuilder pb = new ProcessBuilder(
	                ffmpegPath, 
	                "-i", videoFilePath, 
	                "-vf", "fps=10,scale=320:-1:flags=lanczos", // Create GIF from video
	                "-ss", String.valueOf(startTime), // Start from 'startTime'
	                "-t", String.valueOf(gifDuration), // Duration of GIF
	                outputGifPath
	            );
	            try {
	                Process process = pb.start();
	                process.waitFor();
	                System.out.println("GIF created: " + outputGifPath);
	            } catch (IOException | InterruptedException e) {
	                e.printStackTrace();
	            }
	            startTime += gifDuration; // Move to the next segment
	        }
	    }

	    public static int getVideoDuration(String videoFilePath) {
	        // This method assumes that you have ffmpeg installed and properly working
	        // The implementation of this method depends on ffmpeg command-line to get the video duration.
	        try {
	            ProcessBuilder pb = new ProcessBuilder(
	                "tools/ffmpeg.exe", 
	                "-i", videoFilePath
	            );
	            Process process = pb.start();
	            // For simplicity, return a fixed duration. You could parse the output of ffmpeg for the real duration.
	            return 60;  // Assume a video duration of 60 seconds (modify as needed)
	        } catch (IOException e) {
	            e.printStackTrace();
	            return 0; // Return 0 if duration cannot be determined
	        }
	    }
	}

	
	

