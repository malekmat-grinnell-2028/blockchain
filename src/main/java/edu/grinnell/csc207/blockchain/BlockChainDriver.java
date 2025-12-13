package edu.grinnell.csc207.blockchain;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * The main driver for the block chain program.
 */
public class BlockChainDriver {

    /**
     * The main entry point for the program.
     * 
     * @param args the command-line arguments
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException { 
        if (args.length != 1) {
            System.err.println("Usage: mvn exec:args -Dexec.args=\"<intial amount>\"");
        }
        int initial = Integer.parseInt(args[0]);
        BlockChain bChain = new BlockChain(initial);
        boolean running = true;
        Scanner scan = new Scanner(System.in);
        while (running) {
            System.out.println(bChain.toString());
            String input = scan.nextLine();
            if (input.equals("quit")) {
                running = false;
            } else if (input.equals("help")) {
                System.out.println("Valid Commands:\n"
                        + "    mine: discovers the nonce for a given transaction\n"
                        + "    append: appends a new block onto the end of the chain\n"
                        + "    remove: removes the last block from the end of the chain\n"
                        + "    check: checks that the block chain is valid\n"
                        + "    report: reports the balances of Alice and Bob\n"
                        + "    help: prints this list of commands\n"
                        + "    quit: quits the program");
            } else if (input.equals("mine")) {
                System.out.println("Amount transferred? ");
                input = scan.nextLine();
                int amount = Integer.parseInt(input);
                System.out.print("amount = " + amount + ", Nonce = ");
                System.out.println(bChain.mine(amount).getNonce());
            } else if (input.equals("append")) {
                System.out.println("Amount transferred? ");
                input = scan.nextLine();
                int amount = Integer.parseInt(input);
                System.out.println("Nonce? ");
                input = scan.nextLine();
                int nonce = Integer.parseInt(input);
                Block b = new Block(bChain.getSize() + 1, amount, bChain.getHash(), nonce);
                bChain.append(b);
            } else if (input.equals("remove")) {
                bChain.removeLast();
            } else if (input.equals("check")) {
                if (bChain.isValidBlockChain()) {
                    System.out.println("Chain is valid!");
                } else {
                    System.out.println("Chain is not valid");
                }
            } else if (input.equals("report")) {
                bChain.printBalances();
            } else {
                System.out.println("Unknown Command");
            }
        }
        scan.close();
    }
}
