package org.example;

/**
 * Unique IP address Counter Execution
 *
 */
public class App 
{
    public static void main( String[] args ) {
        System.out.println("Unique IP addresses: " + UniqueIpAddressCounter.count("ip_addresses.txt"));
        System.out.println("Unique IP addresses: " + UniqueIpAddressCounter.count("ip_addresses2.txt"));
        System.out.println("Unique IP addresses: " + UniqueIpAddressCounter.count("ip_addresses3.txt"));
        System.out.println("Unique IP addresses: " + UniqueIpAddressCounter.count("ip_addresses4.txt"));

        // Execution of this file takes about 29 minutes.
        // The file weights about 20Gb, and unzips to about 120Gb.
//        System.out.println("Unique IP addresses: " + UniqueIpAddressCounter.count("ip_addresses.zip"));
    }
}
