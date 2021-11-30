package com.yahoo.ycsb;
import java.io.*;

/**
 */
public final class WorkloadsWriteFile{
  public static final int INSERT = 0;
  public static final int DELETE = 1;
  public static final int UPDATE = 2;
  public static final int READ = 3;
  public static final int SCAN = 4;
    
  private WorkloadsWriteFile(){

  }
  public static void appendWorkloadToFile(OutputStream out, int type, String key, String value){
    int typesize = 1;
    int keysize = key.length(); 
    int valsize = value.length();
    int encodedlen;
    switch(type){
    case INSERT:
    case UPDATE:
      encodedlen = typesize + VariableLength.variableLengthSize(keysize) +
        keysize + VariableLength.variableLengthSize(valsize) +
        valsize;
      break;
    case DELETE:
    case READ:
      encodedlen = typesize + VariableLength.variableLengthSize(keysize) +
        keysize;
      break;
    case SCAN:
      encodedlen = typesize + VariableLength.variableLengthSize(keysize) +
        keysize;
      break;
    default:
      encodedlen = typesize + VariableLength.variableLengthSize(keysize) +
        keysize + VariableLength.variableLengthSize(valsize) +
        valsize;
      break; 
    }
    byte []res = new byte[encodedlen]; 
    byte []chkey = key.getBytes();
    byte []chval = value.getBytes();
    int index = 0;
    switch(type){
    case INSERT:
    case UPDATE:
      res[index++] = 'i';
      index = VariableLength.writeVariableLengthInt(keysize, res, index);
      System.arraycopy(chkey, 0, res, index, keysize);
      index += keysize;
      index = VariableLength.writeVariableLengthInt(valsize, res, index);
      System.arraycopy(chval, 0, res, index, valsize);
      index += valsize;
      break;
    case DELETE:
      res[index++] = 'd';
      index = VariableLength.writeVariableLengthInt(keysize, res, index);
      System.arraycopy(chkey, 0, res, index, keysize);
      index += keysize;
      break;
    case READ:
      res[index++] = 'r';
      index = VariableLength.writeVariableLengthInt(keysize, res, index);
      System.arraycopy(chkey, 0, res, index, keysize);
      index += keysize;
      break;
    case SCAN:
      res[index++] = 's';
      index = VariableLength.writeVariableLengthInt(keysize, res, index);
      System.arraycopy(chkey, 0, res, index, keysize);
      index += keysize;
      break;
    default:
      break; 
    }
    try {
      out.write(res);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public static void appendWorkloadToFile1(FileWriter writer, int type, String key, 
                                          String value, FileWriter writerread){
    String wtype;
    try{
      switch(type){
      case INSERT:
        wtype = "insert";
        writer.write(wtype);
        writer.write("\r\n");
        writer.write(key);
        writer.write("\r\n");
        writer.write(value);
        writer.write("\r\n");
        break;
      case UPDATE:
        wtype = "insert";
        writer.write(wtype);
        writer.write("\r\n");
        writer.write(key);
        writer.write("\r\n");
        writer.write(value);
        writer.write("\r\n");
        //wtype = "read";
        //writerread.write(wtype);
        //writerread.write("\r\n");
        //writerread.write(key);
        //writerread.write("\r\n");
        //writerread.flush();
        break;
      case DELETE:
        wtype = "delete";
        writer.write(wtype);
        writer.write("\r\n");
        writer.write(key);
        writer.write("\r\n");
        break;
      case READ:
        wtype = "read";
        writer.write(wtype);
        writer.write("\r\n");
        writer.write(key);
        writer.write("\r\n");
        break;
      case SCAN:
        wtype = "scan";
        writer.write(wtype);
        writer.write("\r\n");
        writer.write(key);
        writer.write("\r\n");
        break;
      default:
        break; 
      }
      writer.flush();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
