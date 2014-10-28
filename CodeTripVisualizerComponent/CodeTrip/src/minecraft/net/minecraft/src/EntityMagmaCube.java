package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class EntityMagmaCube extends EntitySlime
{
    public EntityMagmaCube(World par1World)
    {
        super(par1World);
        texture = "/mob/lava.png";
        isImmuneToFire = true;
        landMovementFactor = 0.2F;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return worldObj.difficultySetting > 0 && worldObj.checkIfAABBIsClear(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty() && !worldObj.isAnyLiquid(boundingBox);
    }

    /**
     * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
     */
    public int getTotalArmorValue()
    {
        return getSlimeSize() * 3;
    }

    public int getBrightnessForRender(float par1)
    {
        return 0xf000f0;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float par1)
    {
        return 1.0F;
    }

    /**
     * Returns the name of a particle effect that may be randomly created by EntitySlime.onUpdate()
     */
    protected String getSlimeParticle()
    {
        return "flame";
    }

    protected EntitySlime createInstance()
    {
        return new EntityMagmaCube(worldObj);
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return Item.magmaCream.shiftedIndex;
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean par1, int par2)
    {
        int i = getDropItemId();

        if (i > 0 && getSlimeSize() > 1)
        {
            int j = rand.nextInt(4) - 2;

            if (par2 > 0)
            {
                j += rand.nextInt(par2 + 1);
            }

            for (int k = 0; k < j; k++)
            {
                dropItem(i, 1);
            }
        }
    }

    /**
     * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     */
    public boolean isBurning()
    {
        return false;
    }

    /**
     * Gets the amount of time the slime needs to wait between jumps.
     */
    protected int getJumpDelay()
    {
        return super.getJumpDelay() * 4;
    }

    protected void func_70808_l()
    {
        field_70813_a *= 0.9F;
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jump()
    {
        motionY = 0.42F + (float)getSlimeSize() * 0.1F;
        isAirBorne = true;
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float f)
    {
    }

    /**
     * Indicates weather the slime is able to damage the player (based upon the slime's size)
     */
    protected boolean canDamagePlayer()
    {
        return true;
    }

    /**
     * Gets the amount of damage dealt to the player when "attacked" by the slime.
     */
    protected int getAttackStrength()
    {
        return super.getAttackStrength() + 2;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.slime";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.slime";
    }

    /**
     * Returns the name of the sound played when the slime jumps.
     */
    protected String getJumpSound()
    {
        if (getSlimeSize() > 1)
        {
            return "mob.magmacube.big";
        }
        else
        {
            return "mob.magmacube.small";
        }
    }

    /**
     * Whether or not the current entity is in lava
     */
    public boolean handleLavaMovement()
    {
        return false;
    }

    /**
     * Returns true if the slime makes a sound when it lands after a jump (based upon the slime's size)
     */
    protected boolean makesSoundOnLand()
    {
        return true;
    }
}