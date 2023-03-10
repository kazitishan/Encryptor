public class Encryptor
{
    /** A two-dimensional array of single-character strings, instantiated in the constructor */
    private String[][] letterBlock;

    /** The number of rows of letterBlock, set by the constructor */
    private int numRows;

    /** The number of columns of letterBlock, set by the constructor */
    private int numCols;

    /** Constructor*/
    public Encryptor(int r, int c)
    {
        letterBlock = new String[r][c];
        numRows = r;
        numCols = c;
    }

    public String[][] getLetterBlock()
    {
        return letterBlock;
    }

    /** Places a string into letterBlock in row-major order.
     *
     *   @param str  the string to be processed
     *
     *   Postcondition:
     *     if str.length() < numRows * numCols, "A" in each unfilled cell
     *     if str.length() > numRows * numCols, trailing characters are ignored
     */
    public void fillBlock(String str)
    {
        // making str equal to the length of the number of blocks:
        if (str.length() > numRows * numCols) str = str.substring(0, numRows * numCols);
        if (str.length() < numRows * numCols){
            int additionAs = (numRows * numCols) - str.length();
            for (int i = 0; i < additionAs; i++){
                str += "A";
            }
        }

        int charNum = 0;
        for (int row = 0; row < letterBlock.length; row++){
            for (int col = 0; col < letterBlock[0].length; col++){
                letterBlock[row][col] = str.charAt(charNum) + "";
                charNum++;
            }
        }
    }


    /** Extracts encrypted string from letterBlock in column-major order.
     *
     *   Precondition: letterBlock has been filled
     *
     *   @return the encrypted string from letterBlock
     */
    public String encryptBlock()
    {
        String encrypted = "";
        for (int col = 0; col < letterBlock[0].length; col++){
            for (int row = 0; row < letterBlock.length; row++){
                encrypted += letterBlock[row][col];
            }
        }
        return encrypted;
    }


    /** Encrypts a message.
     *
     *  @param message the string to be encrypted
     *
     *  @return the encrypted message; if message is the empty string, returns the empty string
     */
    public String encryptMessage(String message)
    {
        String encrypted = "";
        double iterations = (double) message.length() / (numRows * numCols);
        while (iterations % 1 != 0){
            message += "A";
            iterations = (double) message.length() / (numRows * numCols);
        }
        for (int i = 0; i < iterations; i++){
            fillBlock(message);
            message = message.substring(numRows * numCols);
            encrypted += encryptBlock();
        }
        return encrypted;
    }

    /**  Decrypts an encrypted message. All filler 'A's that may have been
     *   added during encryption will be removed, so this assumes that the
     *   original message (BEFORE it was encrypted) did NOT end in a capital A!
     *
     *   NOTE! When you are decrypting an encrypted message,
     *         be sure that you have initialized your Encryptor object
     *         with the same row/column used to encrypted the message! (i.e.
     *         the “encryption key” that is necessary for successful decryption)
     *         This is outlined in the precondition below.
     *
     *   Precondition: the Encryptor object being used for decryption has been
     *                 initialized with the same number of rows and columns
     *                 as was used for the Encryptor object used for encryption.
     *
     *   @param encryptedMessage  the encrypted message to decrypt
     *
     *   @return  the decrypted, original message (which had been encrypted)
     *
     *   TIP: You are encouraged to create other helper methods as you see fit
     *        (e.g. a method to decrypt each section of the decrypted message,
     *         similar to how encryptBlock was used)
     */
    public String decryptMessage(String encryptedMessage)
    {
        int iterations = encryptedMessage.length() / (numRows * numCols);
        String message = "";
        for (int i = 0; i < iterations; i++){
            decryptFill(encryptedMessage.substring(0, numRows * numCols));
            for (int row = 0; row < letterBlock.length; row++){
                for (int col = 0; col < letterBlock[0].length; col++){
                    message += letterBlock[row][col];
                }
            }
            encryptedMessage = encryptedMessage.substring(numRows * numCols);
        }
        while (message.substring(message.length() - 1).equals("A")){
            message = message.substring(0, message.length() - 1);
        }
        return message;
    }

    private void decryptFill(String str){
        int charNum = 0;
        for (int col = 0; col < letterBlock[0].length; col++){
            for (int row = 0; row < letterBlock.length; row++){
                letterBlock[row][col] = str.charAt(charNum) + "";
                charNum++;
            }
        }
    }

    public String superEncryptMessage(String message){
        // 64bit values:
        String values = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz+/";
        String shifted = "";
        int shift = 7;
        for (int i = 0; i < message.length(); i++){
            String shiftedCharacter = "";
            if (i + shift >= values.length()){
                shiftedCharacter = values.charAt((i + shift) - values.length()) + "";
            }
            else shiftedCharacter = values.charAt(i + shift) + "";
            shifted += shiftedCharacter;
        }
        return encryptMessage(shifted);
    }
}