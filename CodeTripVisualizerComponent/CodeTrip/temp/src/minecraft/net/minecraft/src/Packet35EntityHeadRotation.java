// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet35EntityHeadRotation extends Packet
{

    public int field_73383_a;
    public byte field_73382_b;

    public Packet35EntityHeadRotation()
    {
    }

    public Packet35EntityHeadRotation(int p_i3343_1_, byte p_i3343_2_)
    {
        field_73383_a = p_i3343_1_;
        field_73382_b = p_i3343_2_;
    }

    public void func_73267_a(DataInputStream p_73267_1_)
        throws IOException
    {
        field_73383_a = p_73267_1_.readInt();
        field_73382_b = p_73267_1_.readByte();
    }

    public void func_73273_a(DataOutputStream p_73273_1_)
        throws IOException
    {
        p_73273_1_.writeInt(field_73383_a);
        p_73273_1_.writeByte(field_73382_b);
    }

    public void func_73279_a(NetHandler p_73279_1_)
    {
        p_73279_1_.func_72478_a(this);
    }

    public int func_73284_a()
    {
        return 5;
    }

    public boolean func_73278_e()
    {
        return true;
    }

    public boolean func_73268_a(Packet p_73268_1_)
    {
        Packet35EntityHeadRotation packet35entityheadrotation = (Packet35EntityHeadRotation)p_73268_1_;
        return packet35entityheadrotation.field_73383_a == field_73383_a;
    }

    public boolean func_73277_a_()
    {
        return true;
    }
}
