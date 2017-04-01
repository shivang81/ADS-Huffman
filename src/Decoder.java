import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Decoder for the encoded huffman coded file
 *
 * Created by shivanggupta on 30/03/17.
 */
public class decoder {

    public static void decode(StringBuilder encodedString, Map<String, String> codeTable) {
        HashMap<String, String> map = new HashMap<>();
        for(Map.Entry<String,String> entry : codeTable.entrySet()) {
            map.put(entry.getValue(), entry.getKey());
        }
        String temp = "";
        try {
            String FILENAME = "decode.txt";
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME));
            for(int i = 0; i < encodedString.length(); i++) {
                temp += encodedString.charAt(i);
                if(map.containsKey(temp)) {
                    bw.write(map.get(temp));
                    bw.newLine();
                    temp = "";
                }
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static byte[] readAndClose(InputStream aInput){
        byte[] bucket = new byte[32*1024];
        ByteArrayOutputStream result = null;
        try  {
            try {
                result = new ByteArrayOutputStream(bucket.length);
                int bytesRead = 0;
                while(bytesRead != -1){
                    bytesRead = aInput.read(bucket);
                    if(bytesRead > 0){
                        result.write(bucket, 0, bytesRead);
                    }
                }
            }
            finally {
                aInput.close();
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        return result != null ? result.toByteArray() : null;
    }

    public static void main(String[] args) {
        String encodedFile = args[0];
        String codeTableFile = args[1];
        Map<String, String> codeTable = new HashMap<>();
        try (Stream<String> stream = Files.lines(Paths.get(codeTableFile))) {
            stream.forEach(entry -> {
                String[] s = entry.split(" ");
                codeTable.put(s[0], s[1]);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(encodedFile);
        System.out.println("File size: " + file.length());
        byte[] result;
        StringBuilder encoded = new StringBuilder();
        try {
            InputStream input =  new BufferedInputStream(new FileInputStream(file));
            result = readAndClose(input);
            for (byte b : result) {
                String s = Integer.toBinaryString(b);
                String sub;
                if(s.length() == 32)
                    sub = s.substring(24, 32);
                else {
                    if(s.length() != 8) {
                        String temp = "";
                        for(int i = 0; i < 8 - s.length(); i++) {
                            temp += "0";
                        }
                        s = temp + s;
                    }
                    sub = s;
                }
                encoded.append(sub);
            }
            decode(encoded, codeTable);
        }

        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
}
