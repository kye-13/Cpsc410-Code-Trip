// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            EntityCreature, DataWatcher, NBTTagCompound, World

public abstract class EntityAgeable extends EntityCreature
{

    public EntityAgeable(World p_i3436_1_)
    {
        super(p_i3436_1_);
    }

    protected void func_70088_a()
    {
        super.func_70088_a();
        field_70180_af.func_75682_a(12, new Integer(0));
    }

    public int func_70874_b()
    {
        return field_70180_af.func_75679_c(12);
    }

    public void func_70873_a(int p_70873_1_)
    {
        field_70180_af.func_75692_b(12, Integer.valueOf(p_70873_1_));
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);
        p_70014_1_.func_74768_a("Age", func_70874_b());
    }

    public void func_70037_a(NBTTagCompound p_70037_1_)
    {
        super.func_70037_a(p_70037_1_);
        func_70873_a(p_70037_1_.func_74762_e("Age"));
    }

    public void func_70636_d()
    {
        super.func_70636_d();
        int i = func_70874_b();
        if(i < 0)
        {
            i++;
            func_70873_a(i);
        } else
        if(i > 0)
        {
            i--;
            func_70873_a(i);
        }
    }

    public boolean func_70631_g_()
    {
        return func_70874_b() < 0;
    }
}
