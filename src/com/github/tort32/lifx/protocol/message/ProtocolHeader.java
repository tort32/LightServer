package com.github.tort32.lifx.protocol.message;

import com.github.tort32.lifx.protocol.InBuffer;
import com.github.tort32.lifx.protocol.OutBuffer;
import com.github.tort32.lifx.protocol.Types.UInt16;
import com.github.tort32.lifx.protocol.Types.UInt64;

public class ProtocolHeader {
	public UInt64 mReserved1 = new UInt64(0); // 64 bits. Reserved
    public UInt16 mType = new UInt16(0); // 16 bits. ProtocolMessage type determines the payload being used
    public UInt16 mReserved2 = new UInt16(0); // 16 bits. Reserved

    public static final int LENGTH = 12;

    public ProtocolHeader()
    {
    }

    public ProtocolHeader(InBuffer buffer)
    {
      buffer.checkLength(LENGTH);
      mReserved1 = buffer.readUInt64();
      mType = buffer.readUInt16();
      mReserved2 = buffer.readUInt16();
    }

    public void write(OutBuffer buffer) {
    	buffer
    		.beginChunk()
			.write(mReserved1)
			.write(mType)
			.write(mReserved2)
			.endChunk(LENGTH);
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(getClass().getSimpleName() + " {\n");
      sb.append("  mReserved1=" + mReserved1 + "\n");
      sb.append("  mType=" + mType + "\n");
      sb.append("  mReserved2=" + mReserved2 + "\n");
      sb.append("}");
      return sb.toString();
    }
}
