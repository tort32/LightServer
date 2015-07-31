package com.github.tort32.lifx.protocol.message;

import com.github.tort32.lifx.protocol.InBuffer;
import com.github.tort32.lifx.protocol.OutBuffer;

public class Message {
	public Frame mFrame;
    public FrameAddress mFrameAddress;
    public ProtocolHeader mProtocolHeader;
    public Payload mPayload;

    public Message(Payload payload)
    {
      if (payload == null)
      {
        throw new NullPointerException("payload");
      }
      mFrame = new Frame();
      mFrameAddress = new FrameAddress();
      mProtocolHeader = new ProtocolHeader();
      mPayload = payload;
      payload.setHeader(mFrame, mFrameAddress, mProtocolHeader);
    }

    public Message(byte[] inBuffer)
    {
      InBuffer buffer = new InBuffer(inBuffer);
      mFrame = new Frame(buffer);
      mFrameAddress = new FrameAddress(buffer);
      mProtocolHeader = new ProtocolHeader(buffer);

      // Payload factory
      switch (mProtocolHeader.mType.getInt())
      {
        /*case StateService.ID:
          mPayload = new StateService(buffer);
          break;
        case State.ID:
          mPayload = new State(buffer);
          break;*/
      }
    }

    public byte[] toArray()
    {
      OutBuffer buffer = new OutBuffer();
      buffer.beginChunk();

      mFrame.write(buffer);
      mFrameAddress.write(buffer);
      mProtocolHeader.write(buffer);
      mPayload.write(buffer);

      buffer.endChunk(Payload.OFFSET + mPayload.length);

      byte[] packet = buffer.toArray();

      // Finally inscribe total packet length value
      int size = packet.length;
      packet[0] = (byte)(size & 0x00FF);
      packet[1] = (byte)((size & 0xFF00) >> 8);

      return packet;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(mFrame + "\n");
      sb.append(mFrameAddress + "\n");
      sb.append(mProtocolHeader + "\n");
      sb.append(mPayload + "\n");
      return sb.toString();
    }
}
