package converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class VideoToGifConverter {
    public static void main(String[] args) {
        String videoUrl = "https://youtube.com/shorts/uJhmpwBT68I?si=supPELiPZpF_tRS2"; // Replace with actual video URL
        String outputVideoPath = "tools/downloaded_video.mp4"; // Path to save the downloaded video
        String gifOutputPath = "tools/output.gif"; // Path to save the final GIF

        try {
            // Step 1: Download video using yt-dlp.exe
            System.out.println("Downloading video...");
            boolean isDownloaded = downloadVideo(videoUrl, outputVideoPath);

            if (isDownloaded) {
                System.out.println("Video downloaded successfully!");
                // Step 2: Convert the video to GIF using FFmpeg
                System.out.println("Converting video to GIF...");
                boolean isConverted = convertToGif(outputVideoPath, gifOutputPath);

                if (isConverted) {
                    System.out.println("GIF created successfully: " + gifOutputPath);
                } else {
                    System.err.println("Failed to create GIF.");
                }
            } else {
                System.err.println("Failed to download video.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to download video using yt-dlp.exe
    public static boolean downloadVideo(String videoUrl, String outputPath) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(
            "tools/yt-dlp.exe", videoUrl, "-o", outputPath
        );
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        return exitCode == 0;
    }

    // Method to convert video to GIF using FFmpeg
    public static boolean convertToGif(String inputVideoPath, String outputGifPath) throws Exception {
       
    	
    	// Example path to save each GIF in the output_gifs folder
    	outputGifPath = "output_gifs" + System.currentTimeMillis() + ".gif";  // Unique file name based on timestamp

    	// Update the ProcessBuilder command with the new output path
    	ProcessBuilder processBuilder = new ProcessBuilder(
    	    "tools/ffmpeg.exe",
    	    "-i", inputVideoPath,                     // input video file
    	    "-vf", "fps=10,scale=320:-1:flags=lanczos", // filter for GIF conversion
    	    "-loop", "0",                             // looping
    	    outputGifPath                              // output GIF file path
    	);

    	
//    	
//    	ProcessBuilder processBuilder = new ProcessBuilder(
//            "tools/ffmpeg.exe",
//            "-i", inputVideoPath,      // Input video file
//            "-vf", "fps=10,scale=320:-1:flags=lanczos", // Filters: frame rate and scaling
//            "-loop", "0",              // Loop indefinitely
//            outputGifPath              // Output GIF file
//        );
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        return exitCode == 0;
    }
}
