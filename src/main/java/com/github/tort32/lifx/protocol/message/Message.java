package com.github.tort32.lifx.protocol.message;

import com.github.tort32.lifx.protocol.InBuffer;
import com.github.tort32.lifx.protocol.OutBuffer;

public class Message {
	
	public static final long SENDER_ID = 0xC0FEBABEL;
	
	public Frame mFrame;
    public FrameAddress mFrameAddress;
    public ProtocolHeader mProtocolHeader;
    public Payload mPayload;

    public Message(Payload payload) {
      if (payload == null) {
        throw new NullPointerException("Payload can't be null");
      }
      mFrame = new Frame();
      mFrameAddress = new FrameAddress();
      mProtocolHeader = new ProtocolHeader();
      mPayload = payload;
      payload.setHeader(mFrame, mFrameAddress, mProtocolHeader);
      
    }

    public Message(byte[] inBuffer) {
		InBuffer buffer = new InBuffer(inBuffer);
		mFrame = new Frame(buffer);
		mFrameAddress = new FrameAddress(buffer);
		mProtocolHeader = new ProtocolHeader(buffer);
		int payloadId = mProtocolHeader.mType.getValue();
		mPayload = PayloadFactory.createFromBuffer(payloadId, buffer);
    }
    
    public void setBroadcast() {
    	mFrame.mTagged = 1;
    }
    
    public void setSource(long senderId) {
    	mFrame.mSource.set(senderId); // Need to filter response messages
    }
    
    public void setTarget(String mac) {
    	mFrame.mTagged = 0;
    	mFrameAddress.mTarget.set(mac);
    }
    
    public void requestAcknowledgement() {
    	mFrameAddress.mAckRequired = 1;
    }

    public byte[] toArray()
    {
      OutBuffer buffer = new OutBuffer(Payload.OFFSET + mPayload.length);
      buffer.beginChunk();
      mFrame.write(buffer);
      mFrameAddress.write(buffer);
      mProtocolHeader.write(buffer);
      mPayload.write(buffer);
      buffer.endChunk(Payload.OFFSET + mPayload.length);
      return buffer.toArray();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(mFrame + "\n");
      sb.append(mFrameAddress + "\n");
      sb.append(mProtocolHeader + "\n");
      sb.append("Payload = " + mPayload + "\n");
      return sb.toString();
    }
}
