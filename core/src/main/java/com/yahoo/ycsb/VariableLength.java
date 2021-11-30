package com.yahoo.ycsb;

/**
 * 
 */
public final class VariableLength {
  private VariableLength(){

  }

  public static int variableLengthSize(int value){
    //获取相应的int值的可变长度 
    int size = 1;
    while ((value & (~0x7f)) != 0) {
      value >>>= 7;
      size++;
    }
    return size;
  }

  public static int writeVariableLengthInt(int value, byte[] buf, int index){
    int highBitMask = 0x80;
    if (value < (1 << 7) && value >= 0) {
      buf[index++] = (byte)(value);
    }else if (value < (1 << 14) && value > 0) {
      buf[index++] = (byte)(value | highBitMask);
      buf[index++] = (byte)(value >>> 7);
    }else if (value < (1 << 21) && value > 0) {
      buf[index++] = (byte)(value | highBitMask);
      buf[index++] = (byte)((value >>> 7) | highBitMask);
      buf[index++] = (byte)(value >>> 14);
    }else if (value < (1 << 28) && value > 0) {
      buf[index++] = (byte)(value | highBitMask);
      buf[index++] = (byte)((value >>> 7) | highBitMask);
      buf[index++] = (byte)((value >>> 14) | highBitMask);
      buf[index++] = (byte)(value >>> 21);
    }else {
      buf[index++] = (byte)(value | highBitMask);
      buf[index++] = (byte)((value >>> 7) | highBitMask);
      buf[index++] = (byte)((value >>> 14) | highBitMask);
      buf[index++] = (byte)((value >>> 21) | highBitMask);
      buf[index++] = (byte)(value >>> 28);
    }
    return index;
  }

  public static int readVariableLengthInt(byte[] buf, int begin, int len){
    int result = 0;
    int index = 0;
    for (int shift = begin; shift < begin + len; shift++, index++) {
      int b = (short)(buf[shift] & 0xFF);
      // add the lower 7 bits to the result
      result |= ((b & 0x7f) << (index * 7));

      // if high bit is not set, this is the last byte in the number
      if ((b & 0x80) == 0) {
        return result;
      }
    }
    throw new NumberFormatException("last byte of variable length int has high bit set");
  }
}

