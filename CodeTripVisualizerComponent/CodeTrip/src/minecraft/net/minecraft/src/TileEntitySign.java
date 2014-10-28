package net.minecraft.src;

public class TileEntitySign extends TileEntity
{
    public String signText[] =
    {
        "", "", "", ""
    };

    /**
     * The index of the line currently being edited. Only used on client side, but defined on both. Note this is only
     * really used when the > < are going to be visible.
     */
    public int lineBeingEdited;
    private boolean isEditable;

    public TileEntitySign()
    {
        lineBeingEdited = -1;
        isEditable = true;
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setString("Text1", signText[0]);
        par1NBTTagCompound.setString("Text2", signText[1]);
        par1NBTTagCompound.setString("Text3", signText[2]);
        par1NBTTagCompound.setString("Text4", signText[3]);
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        isEditable = false;
        super.readFromNBT(par1NBTTagCompound);

        for (int i = 0; i < 4; i++)
        {
            signText[i] = par1NBTTagCompound.getString((new StringBuilder()).append("Text").append(i + 1).toString());

            if (signText[i].length() > 15)
            {
                signText[i] = signText[i].substring(0, 15);
            }
        }
    }

    /**
     * signs and mobSpawners use this to send text and meta-data
     */
    public Packet getAuxillaryInfoPacket()
    {
        String as[] = new String[4];
        System.arraycopy(signText, 0, as, 0, 4);
        return new Packet130UpdateSign(xCoord, yCoord, zCoord, as);
    }

    public boolean isEditable()
    {
        return isEditable;
    }

    /**
     * Sets the sign's isEditable flag to the specified parameter.
     */
    public void setEditable(boolean par1)
    {
        isEditable = par1;
    }
}