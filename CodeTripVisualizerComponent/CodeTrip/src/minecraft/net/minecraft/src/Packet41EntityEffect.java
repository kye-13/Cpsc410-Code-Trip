package net.minecraft.src;

import java.io.*;

public class Packet41EntityEffect extends Packet
{
    public int entityId;
    public byte effectId;

    /** The effect's amplifier. */
    public byte effectAmplifier;
    public short duration;

    public Packet41EntityEffect()
    {
    }

    public Packet41EntityEffect(int par1, PotionEffect par2PotionEffect)
    {
        entityId = par1;
        effectId = (byte)(par2PotionEffect.getPotionID() & 0xff);
        effectAmplifier = (byte)(par2PotionEffect.getAmplifier() & 0xff);
        duration = (short)par2PotionEffect.getDuration();
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        entityId = par1DataInputStream.readInt();
        effectId = par1DataInputStream.readByte();
        effectAmplifier = par1DataInputStream.readByte();
        duration = par1DataInputStream.readShort();
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(entityId);
        par1DataOutputStream.writeByte(effectId);
        par1DataOutputStream.writeByte(effectAmplifier);
        par1DataOutputStream.writeShort(duration);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleEntityEffect(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 8;
    }

    /**
     * only false for the abstract Packet class, all real packets return true
     */
    public boolean isRealPacket()
    {
        return true;
    }

    /**
     * eg return packet30entity.entityId == entityId; WARNING : will throw if you compare a packet to a different packet
     * class
     */
    public boolean containsSameEntityIDAs(Packet par1Packet)
    {
        Packet41EntityEffect packet41entityeffect = (Packet41EntityEffect)par1Packet;
        return packet41entityeffect.entityId == entityId && packet41entityeffect.effectId == effectId;
    }
}
