package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This class provides methods for counting unique IP addresses found in the specified input file.
 *
 * @author Mary Nazaretyan
 */
public class UniqueIpAddressCounter {
    /**
     * Counts the number of unique IPv4 addresses in the specified file.
     * <p>
     * Each line of the file is expected to contain a single IP address in dot-decimal notation. The method supports the
     * full 32-bit IPv4 address space, using two {@link BitSet}s to handle positive and negative signed integer
     * representations of IP addresses.
     * <p>
     * For performance, a 1MB buffer is used when reading the file, and IP addresses are converted to integers using the
     * {@link #ipToInt(String)} method.
     *
     * @param filePath the path to the input file containing IP addresses (one per line)
     * @return the count of unique IP addresses in the file
     */
    public static long count(String filePath) {
        System.out.println("Processing file: " + filePath);
        // all the possible ip range is 0.0.0.0 to 255.255.255.255
        // 256 * 256 * 256 * 256 = 4,294,967,296 = 2^32
        BitSet bitSet = new BitSet(Integer.MAX_VALUE);       // For IPs < 2^31
        BitSet bitSet2 = new BitSet(Integer.MAX_VALUE);      // For IPs >= 2^31
        long uniqueCount = 0;
        try (BufferedReader reader = createBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                int ip = ipToInt(line.trim());
                if (ip >= 0) {
                    if (!bitSet.get(ip)) {
                        bitSet.set(ip);
                        uniqueCount++;
                    }
                } else {
                    int index = ip & Integer.MAX_VALUE;
                    if (!bitSet2.get(index)) {
                        bitSet2.set(index);
                        uniqueCount++;
                    }
                }
            }
        }
        catch (IOException e) {
            System.err.println("Failed to process file: " + e.getMessage());
        }
        return uniqueCount;
    }

    /**
     * Creates a {@link BufferedReader} for the specified file path.
     * <p>
     * If the file ends with a <code>.gz</code> extension, it is read using a {@link GZIPInputStream}; otherwise, it is
     * read as a regular text file. The method uses a 1MB buffer for efficient reading.
     *
     * @param filePath the path to the input file, which may be plain text or GZIP-compressed
     * @return a {@code BufferedReader} for reading the file content
     * @throws IOException if an I/O error occurs while opening the file or stream
     */
    public static BufferedReader createBufferedReader(String filePath) throws IOException {
        InputStream fileStream = Files.newInputStream(Paths.get(filePath));
        InputStream inputStream;
        if (filePath.endsWith(".gz")) {
            inputStream = new GZIPInputStream(fileStream);
        } else if (filePath.endsWith(".zip")) {
            ZipInputStream zipStream = new ZipInputStream(fileStream);
            ZipEntry entry = zipStream.getNextEntry();
            if (entry == null) {
                throw new IOException("ZIP file is empty: " + filePath);
            }
            inputStream = zipStream;
        } else {
            inputStream = fileStream;
        }
        return new BufferedReader(new InputStreamReader(inputStream), 1024 * 1024);
    }

    /**
     * Converts an IPv4 address in dot-decimal notation to a 32-bit integer.
     * <p>
     * For example, the IP address {@code "192.168.0.1"} will be converted to its corresponding integer representation.
     *
     * @param ip the IPv4 address in dot-decimal format (e.g., {@code "192.168.0.1"})
     * @return the 32-bit integer representation of the IP address
     * @throws NumberFormatException
     *         if the IP address is not properly formatted
     */
    public static int ipToInt(String ip) {
        String[] parts = ip.split("\\.");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid IPv4 address: " + ip);
        }
        return (Integer.parseInt(parts[0]) << 24)
                | (Integer.parseInt(parts[1]) << 16)
                | (Integer.parseInt(parts[2]) << 8)
                | Integer.parseInt(parts[3]);
    }
}
