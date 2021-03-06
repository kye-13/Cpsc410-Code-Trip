package net.minecraft.src;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;

public class DataWatcher
{
    private static final HashMap dataTypes;
    private final Map watchedObjects = new HashMap();

    /** true if one or more object was changed */
    private boolean objectChanged;
    private ReadWriteLock field_75694_d;

    public DataWatcher()
    {
        field_75694_d = new ReentrantReadWriteLock();
    }

    /**
     * adds a new object to dataWatcher to watch, to update an already existing object see updateObject. Arguments: data
     * Value Id, Object to add
     */
    public void addObject(int par1, Object par2Obj)
    {
        Integer integer = (Integer)dataTypes.get(par2Obj.getClass());

        if (integer == null)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("Unknown data type: ").append(par2Obj.getClass()).toString());
        }

        if (par1 > 31)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("Data value id is too big with ").append(par1).append("! (Max is ").append(31).append(")").toString());
        }

        if (watchedObjects.containsKey(Integer.valueOf(par1)))
        {
            throw new IllegalArgumentException((new StringBuilder()).append("Duplicate id value for ").append(par1).append("!").toString());
        }
        else
        {
            WatchableObject watchableobject = new WatchableObject(integer.intValue(), par1, par2Obj);
            field_75694_d.writeLock().lock();
            watchedObjects.put(Integer.valueOf(par1), watchableobject);
            field_75694_d.writeLock().unlock();
            return;
        }
    }

    /**
     * gets the bytevalue of a watchable object
     */
    public byte getWatchableObjectByte(int par1)
    {
        return ((Byte)getWatchedObject(par1).getObject()).byteValue();
    }

    public short getWatchableObjectShort(int par1)
    {
        return ((Short)getWatchedObject(par1).getObject()).shortValue();
    }

    /**
     * gets a watchable object and returns it as a Integer
     */
    public int getWatchableObjectInt(int par1)
    {
        return ((Integer)getWatchedObject(par1).getObject()).intValue();
    }

    /**
     * gets a watchable object and returns it as a String
     */
    public String getWatchableObjectString(int par1)
    {
        return (String)getWatchedObject(par1).getObject();
    }

    /**
     * is threadsafe, unless it throws an exception, then
     */
    private WatchableObject getWatchedObject(int par1)
    {
        field_75694_d.readLock().lock();
        WatchableObject watchableobject;

        try
        {
            watchableobject = (WatchableObject)watchedObjects.get(Integer.valueOf(par1));
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = new CrashReport("getting synched entity data", throwable);
            crashreport.addCrashSection("EntityData ID", Integer.valueOf(par1));
            throw new ReportedException(crashreport);
        }

        field_75694_d.readLock().unlock();
        return watchableobject;
    }

    /**
     * updates an already existing object
     */
    public void updateObject(int par1, Object par2Obj)
    {
        WatchableObject watchableobject = getWatchedObject(par1);

        if (!par2Obj.equals(watchableobject.getObject()))
        {
            watchableobject.setObject(par2Obj);
            watchableobject.setWatched(true);
            objectChanged = true;
        }
    }

    public boolean hasChanges()
    {
        return objectChanged;
    }

    /**
     * writes every object in passed list to dataoutputstream, terminated by 0x7F
     */
    public static void writeObjectsInListToStream(List par0List, DataOutputStream par1DataOutputStream) throws IOException
    {
        if (par0List != null)
        {
            WatchableObject watchableobject;

            for (Iterator iterator = par0List.iterator(); iterator.hasNext(); writeWatchableObject(par1DataOutputStream, watchableobject))
            {
                watchableobject = (WatchableObject)iterator.next();
            }
        }

        par1DataOutputStream.writeByte(127);
    }

    public List unwatchAndReturnAllWatched()
    {
        ArrayList arraylist = null;

        if (objectChanged)
        {
            field_75694_d.readLock().lock();
            Iterator iterator = watchedObjects.values().iterator();

            do
            {
                if (!iterator.hasNext())
                {
                    break;
                }

                WatchableObject watchableobject = (WatchableObject)iterator.next();

                if (watchableobject.isWatched())
                {
                    watchableobject.setWatched(false);

                    if (arraylist == null)
                    {
                        arraylist = new ArrayList();
                    }

                    arraylist.add(watchableobject);
                }
            }
            while (true);

            field_75694_d.readLock().unlock();
        }

        objectChanged = false;
        return arraylist;
    }

    public void writeWatchableObjects(DataOutputStream par1DataOutputStream) throws IOException
    {
        field_75694_d.readLock().lock();
        WatchableObject watchableobject;

        for (Iterator iterator = watchedObjects.values().iterator(); iterator.hasNext(); writeWatchableObject(par1DataOutputStream, watchableobject))
        {
            watchableobject = (WatchableObject)iterator.next();
        }

        field_75694_d.readLock().unlock();
        par1DataOutputStream.writeByte(127);
    }

    public List func_75685_c()
    {
        ArrayList arraylist = null;
        field_75694_d.readLock().lock();
        WatchableObject watchableobject;

        for (Iterator iterator = watchedObjects.values().iterator(); iterator.hasNext(); arraylist.add(watchableobject))
        {
            watchableobject = (WatchableObject)iterator.next();

            if (arraylist == null)
            {
                arraylist = new ArrayList();
            }
        }

        field_75694_d.readLock().unlock();
        return arraylist;
    }

    private static void writeWatchableObject(DataOutputStream par0DataOutputStream, WatchableObject par1WatchableObject) throws IOException
    {
        int i = (par1WatchableObject.getObjectType() << 5 | par1WatchableObject.getDataValueId() & 0x1f) & 0xff;
        par0DataOutputStream.writeByte(i);

        switch (par1WatchableObject.getObjectType())
        {
            case 0:
                par0DataOutputStream.writeByte(((Byte)par1WatchableObject.getObject()).byteValue());
                break;

            case 1:
                par0DataOutputStream.writeShort(((Short)par1WatchableObject.getObject()).shortValue());
                break;

            case 2:
                par0DataOutputStream.writeInt(((Integer)par1WatchableObject.getObject()).intValue());
                break;

            case 3:
                par0DataOutputStream.writeFloat(((Float)par1WatchableObject.getObject()).floatValue());
                break;

            case 4:
                Packet.writeString((String)par1WatchableObject.getObject(), par0DataOutputStream);
                break;

            case 5:
                ItemStack itemstack = (ItemStack)par1WatchableObject.getObject();
                par0DataOutputStream.writeShort(itemstack.getItem().shiftedIndex);
                par0DataOutputStream.writeByte(itemstack.stackSize);
                par0DataOutputStream.writeShort(itemstack.getItemDamage());
                break;

            case 6:
                ChunkCoordinates chunkcoordinates = (ChunkCoordinates)par1WatchableObject.getObject();
                par0DataOutputStream.writeInt(chunkcoordinates.posX);
                par0DataOutputStream.writeInt(chunkcoordinates.posY);
                par0DataOutputStream.writeInt(chunkcoordinates.posZ);
                break;
        }
    }

    public static List readWatchableObjects(DataInputStream par0DataInputStream) throws IOException
    {
        ArrayList arraylist = null;

        for (byte byte0 = par0DataInputStream.readByte(); byte0 != 127; byte0 = par0DataInputStream.readByte())
        {
            if (arraylist == null)
            {
                arraylist = new ArrayList();
            }

            int i = (byte0 & 0xe0) >> 5;
            int j = byte0 & 0x1f;
            WatchableObject watchableobject = null;

            switch (i)
            {
                case 0:
                    watchableobject = new WatchableObject(i, j, Byte.valueOf(par0DataInputStream.readByte()));
                    break;

                case 1:
                    watchableobject = new WatchableObject(i, j, Short.valueOf(par0DataInputStream.readShort()));
                    break;

                case 2:
                    watchableobject = new WatchableObject(i, j, Integer.valueOf(par0DataInputStream.readInt()));
                    break;

                case 3:
                    watchableobject = new WatchableObject(i, j, Float.valueOf(par0DataInputStream.readFloat()));
                    break;

                case 4:
                    watchableobject = new WatchableObject(i, j, Packet.readString(par0DataInputStream, 64));
                    break;

                case 5:
                    short word0 = par0DataInputStream.readShort();
                    byte byte1 = par0DataInputStream.readByte();
                    short word1 = par0DataInputStream.readShort();
                    watchableobject = new WatchableObject(i, j, new ItemStack(word0, byte1, word1));
                    break;

                case 6:
                    int k = par0DataInputStream.readInt();
                    int l = par0DataInputStream.readInt();
                    int i1 = par0DataInputStream.readInt();
                    watchableobject = new WatchableObject(i, j, new ChunkCoordinates(k, l, i1));
                    break;
            }

            arraylist.add(watchableobject);
        }

        return arraylist;
    }

    public void updateWatchedObjectsFromList(List par1List)
    {
        field_75694_d.writeLock().lock();
        Iterator iterator = par1List.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            WatchableObject watchableobject = (WatchableObject)iterator.next();
            WatchableObject watchableobject1 = (WatchableObject)watchedObjects.get(Integer.valueOf(watchableobject.getDataValueId()));

            if (watchableobject1 != null)
            {
                watchableobject1.setObject(watchableobject.getObject());
            }
        }
        while (true);

        field_75694_d.writeLock().unlock();
    }

    static
    {
        dataTypes = new HashMap();
        dataTypes.put(java.lang.Byte.class, Integer.valueOf(0));
        dataTypes.put(java.lang.Short.class, Integer.valueOf(1));
        dataTypes.put(java.lang.Integer.class, Integer.valueOf(2));
        dataTypes.put(java.lang.Float.class, Integer.valueOf(3));
        dataTypes.put(java.lang.String.class, Integer.valueOf(4));
        dataTypes.put(net.minecraft.src.ItemStack.class, Integer.valueOf(5));
        dataTypes.put(net.minecraft.src.ChunkCoordinates.class, Integer.valueOf(6));
    }
}
