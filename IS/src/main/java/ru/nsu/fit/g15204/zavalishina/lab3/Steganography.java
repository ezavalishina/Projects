package ru.nsu.fit.g15204.zavalishina.lab3;

import java.io.*;

class Steganography{

    public static void main(String[] args) {
        File picture = new File("C:\\Users\\Elena Zavalishina\\Desktop\\Научно-учебное\\IS\\3\\picture.jpg");
        encode(picture, "0021");
    }

    public static void encode(File carrier, String payload){
        int pos = locatePixelArray(carrier);
        int readByte = 0;
        try(RandomAccessFile stream = new RandomAccessFile(carrier, "rw")){
            //set the LSB for the first 32 bytes as 0
            stream.seek(pos);
            for(int i = 0; i < 32; i++){
                readByte = stream.read();
                stream.seek(pos);
                stream.write(readByte & 0b11111110);
                pos++;
            }

            //write payload to carrier
            payload += (char) 0;
            int payloadByte;
            int payloadBit;
            int newByte;
            for(char element : payload.toCharArray()){
                payloadByte = (int) element;
                //System.out.println(element + ":" + Integer.toString(character)); //uncomment for debug
                for(int i = 0; i < 8; i++){
                    readByte = stream.read();
                    payloadBit = (payloadByte >> i) & 1;
                    newByte = (readByte & 0b11111110) | payloadBit;
                    stream.seek(pos);
                    stream.write(newByte);
                    pos++;
                }
            }

        } catch(IOException e){
            return;
        }
    }

    public static int locatePixelArray(File file){
        try(FileInputStream stream = new FileInputStream(file)){
            //skip to section in header specifying location
            stream.skip(10);
            //read 4 bytes together as an integer to find the offset of the pixel array
            int location = 0;
            for(int i = 0; i < 4; i++){
                location = location | (stream.read() << (4 * i));
            }
            return location;
        } catch(IOException e){
            return -1;
        }
    }

    public static String decode(File carrier){
        int start = locatePixelArray(carrier);
        //open file
        try(FileInputStream stream = new FileInputStream(carrier)){
            //skip until pixel array starts
            stream.skip(start);

            //check if message has been encoded
            for(int i = 0; i < 32; i++){
                if((stream.read() & 1) != 0){
                    return "";
                }
            }

            //string together consecutive LSB's to obtain the payload
            String result = "";
            int character;
            while(true){
                character = 0;
                for(int i = 0; i < 8; i++){
                    character = character | ((stream.read() & 1) << i);
                }
                if(character == 0) break;
                result += (char) character;
            }

            //clean up and return
            return result;
        } catch(IOException e){
            if(true) //check exception type TODO
                return "Error: file not found";
            else
                return "Error: IO error";
        }
    }

    /**
     * Returns the space available for encoding steganographic messages in bytes.
     */
    public static int charactersAvailable(File carrier){
        return (int) (carrier.length() - locatePixelArray(carrier) + 32) / 8;
    }
}
