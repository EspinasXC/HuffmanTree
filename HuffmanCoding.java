package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        StdIn.setFile(fileName);

        ArrayList<CharFreq> charFreqList = new ArrayList<CharFreq>();//unsorted
        ArrayList<Character> charList = new ArrayList<Character>();//reading
        int count = 0;
        double probability [] = new double[128];
        int probcount = 0;

        while(StdIn.hasNextChar() == true){
            charList.add(StdIn.readChar());
            count++;
        }

        for(int i = 0; i < charList.size(); i++){
            probcount = 1;
            for(int j = i + 1; j < charList.size(); j++){
                
            if(charList.get(i) == charList.get(j)){
                probcount++;
            }
        }
            if(probability[charList.get(i)] == 0){
                probability[charList.get(i)] = probcount;}
        }
        
    for(int i = 0; i < probability.length; i++){
        probability[i] = probability[i] / count;
    }

    for(int i = 0; i < probability.length; i++){
        if(probability[i] != 0.0){
            CharFreq character = new CharFreq();
            character.setCharacter((char) i);
            character.setProbOcc(probability[i]); 
            charFreqList.add(character);
    }}

    if(charFreqList.size() == 1){
        CharFreq character2 = new CharFreq();
        character2.setProbOcc(0); 
        character2.setCharacter((char)(charFreqList.get(0).getCharacter() + 1));

        if(charFreqList.get(0).getCharacter() == (char) 127){
            character2.setCharacter((char) 0);
            character2.setProbOcc(0); 
        }
        charFreqList.add(character2);

    }
    Collections.sort(charFreqList);
    sortedCharFreqList = charFreqList;

    }

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
    public void makeTree() {

        Queue<CharFreq> source = new Queue<CharFreq>();
        Queue<TreeNode> target = new Queue<TreeNode>();

        CharFreq spacer = new CharFreq();
        TreeNode space = new TreeNode();
        TreeNode left = new TreeNode();
        TreeNode right = new TreeNode();

        spacer.setCharacter(null);

        for(int i = 0; i < sortedCharFreqList.size(); i++){
            source.enqueue(sortedCharFreqList.get(i));
        }

            left.setData(source.dequeue());
            right.setData(source.dequeue());
    
            spacer.setProbOcc(left.getData().getProbOcc() + right.getData().getProbOcc());
            space.setData(spacer);
            space.setLeft(left);
            space.setRight(right);
            target.enqueue(space);       
            
        while(!(source.size() == 0 && target.size() == 1)){

            TreeNode space2 = new TreeNode();
            TreeNode left2 = new TreeNode();
            TreeNode right2 = new TreeNode();
            CharFreq spacer2 = new CharFreq();
            spacer2.setCharacter(null);
            spacer2.setProbOcc(0);
    
        
        //Scenario 1
        //s = null and t > 1
        if(source.isEmpty() == true && target.size() > 1){
            left2 = target.dequeue();
            right2 = target.dequeue();
        }   

        //Scenario 2
        //s = 1 and t = 1
        if(source.size() == 1 && target.size() == 1){

            if(source.peek().getProbOcc() <= target.peek().getData().getProbOcc()){
                left2.setData(source.dequeue());
                right2 = target.dequeue();
               }
               else{
                left2 = target.dequeue();
                right2.setData(source.dequeue());
        }
    }
        //Scenario 3
        //s = 1 and t > 1
        if(source.size() == 1 && target.size() > 1){

        if(source.peek().getProbOcc() <= target.peek().getData().getProbOcc()){
            left2.setData(source.dequeue());
            right2 = target.dequeue();
           }

           else{
            left2 = target.dequeue();
            if(source.peek().getProbOcc() <= target.peek().getData().getProbOcc()){
                right2.setData(source.dequeue());
               }
               else{
                right2 = target.dequeue();
               }
    }
}
        //Scenario 4
        //s > 1 and t = 1
        if(source.size() > 1 && target.size() == 1){

        if(source.peek().getProbOcc() <= target.peek().getData().getProbOcc()){
            left2.setData(source.dequeue());

            if(source.peek().getProbOcc() <= target.peek().getData().getProbOcc()){
                right2.setData(source.dequeue());
               }
               else{
                right2 = target.dequeue();
               }
           }
           else{
            left2 = target.dequeue();
            right2.setData(source.dequeue());
    }
}
        //Scenario 5    
        //s > 1 and t > 1
        if(source.size() > 1 && target.size() > 1){
               if(source.peek().getProbOcc() <= target.peek().getData().getProbOcc()){
                left2.setData(source.dequeue());
               }
               else{
                left2 = target.dequeue();
               }
               if(source.peek().getProbOcc() <= target.peek().getData().getProbOcc()){
                right2.setData(source.dequeue());
               }
               else{
                right2 = target.dequeue();
               }
        }
            spacer2.setProbOcc(left2.getData().getProbOcc() + right2.getData().getProbOcc());
            space2.setData(spacer2);
            space2.setLeft(left2);
            space2.setRight(right2);
            
            target.enqueue(space2);
        }
        huffmanRoot = target.dequeue();
    }
	/* Your code goes here */
    

    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */

    public void makeEncodings() {

        String[] ASC = new String[128];
        TreeNode key = new TreeNode();
        String result = new String();
        key = huffmanRoot;

        int count = 0;
        for(int i = 0; i < sortedCharFreqList.size(); i++){

            if(sortedCharFreqList.get(i).getProbOcc() > 0){
                count++;
            }}

        //if more then one character occurs
        if(count > 1){
        for(int i = 0; i < sortedCharFreqList.size(); i++){

            if(sortedCharFreqList.get(i).getProbOcc() > 0){

                ASC[(int)sortedCharFreqList.get(i).getCharacter()] = searchTreeNode(sortedCharFreqList.get(i).getCharacter(), key, result);
            }
            else {
                ASC[(int)sortedCharFreqList.get(i).getCharacter()] = null;
            }
        }}

        //if only 1 occurs
        else{
            for(int i = 0; i < sortedCharFreqList.size(); i++){

                if(sortedCharFreqList.get(i).getProbOcc() > 0){
                    ASC[(int)sortedCharFreqList.get(i).getCharacter()] = "1";
                    ASC[((int)sortedCharFreqList.get(i).getCharacter()) + 1] = "0";
                }
                else {
                    ASC[(int)sortedCharFreqList.get(i).getCharacter()] = null;
                }
            }
        }
        encodings = ASC;
    }

    private String searchTreeNode(Character target, TreeNode current, String result) {

        Character left = current.getLeft().getData().getCharacter();
        Character right = current.getRight().getData().getCharacter();
  
        //Both children are characters
        if(left != null && right != null){
        //found at right
            if(right == target){
                result += "1";
            }
        //found at left
             else if (left == target){
                result += "0";
            }
        }
        //left is a character and right is a null
        else if(left != null && right == null){
            //found at left
            if(left == target){
                result += "0";
            }
            //move to right node
            else{
                result += "1" + searchTreeNode(target, current.getRight(), result);
            }
        }
        //right is a character and left is a null
        else if(right != null && left == null){
            //found at right
            if(right == target){
                result += "1";
        
            }
            //move to left node
            else{
                result += "0" + searchTreeNode(target, current.getLeft(), result);
         
            }
        }
        //if both children are null
        else if (right == null && left == null){
            if(searchBoolean(target, current.getRight(), false) == true){
                result += "1" + searchTreeNode(target, current.getRight(), result);
            }
            if(searchBoolean(target, current.getLeft(), false) == true){
                result += "0" + searchTreeNode(target, current.getLeft(), result);
            }
            
            }
        
    return result;
    }
    private boolean searchBoolean(Character target, TreeNode current, boolean found) {

    Character left = current.getLeft().getData().getCharacter();
    Character right = current.getRight().getData().getCharacter();

    //Both children are characters
    if(left != null && right != null){
    //found at right
        if(right == target){
            found = true;
        }
    //found at left
         else if (left == target){
            found = true;
        }
    }
    //left is a character and right is a null
    else if(left != null && right == null){
        //found at left
        if(left == target){
            found = true;
        }
        //move to right node
        else{
            found = searchBoolean(target, current.getRight(), found);
        }
    }
    //right is a character and left is a null
    else if(right != null && left == null){
        //found at right
        if(right == target){
            found = true;
        }
        //move to left node
        else{
            found = searchBoolean(target, current.getLeft(), found);
        }
    }
    //if both children are null
    else if (right == null && left == null){
        if(searchBoolean(target, current.getRight(), found) == true){
        found = searchBoolean(target, current.getRight(), found);
        }
        if(searchBoolean(target, current.getLeft(), found) == true){
        found = searchBoolean(target, current.getLeft(), found);
        }
    }
    else{
        found = false;
    }
return found;
}

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */

    public void encode(String encodedFile) {

        StdIn.setFile(fileName);
        char start; 
        String start2 = new String();
        //runs until it reads whole file
        while(StdIn.hasNextChar() == true){
            start = StdIn.readChar();
            start2 += encodings[(int) start]; 
        }
        //put start2 into encoded file
        writeBitString(encodedFile, start2);
    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {

        //initialize files
        StdIn.setFile(encodedFile);
        StdOut.setFile(decodedFile);
        
        //initialize strings
        String encoded = new String();
        encoded = readBitString(encodedFile);
        String decoded = new String();

        //initialize indexes
        TreeNode current = new TreeNode();
        current = huffmanRoot;
        char iterate;
        int i = 0;

        while(i < (encoded.length())){
            iterate = encoded.charAt(i);

            //moves left
            if(iterate == '0'){
                current = current.getLeft();
            }
            //moves right
            if(iterate == '1'){
                current = current.getRight();
            }
            //after moving will check for character
            if(current.getData().getCharacter() != null){
                decoded += current.getData().getCharacter();
                current = huffmanRoot;
            }
            i++;
        }
        StdOut.print(decoded);
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}
