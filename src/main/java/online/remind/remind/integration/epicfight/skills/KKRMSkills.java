package online.remind.remind.integration.epicfight.skills;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import online.remind.remind.KingdomKeysReMind;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.Skill;

public class KKRMSkills {
    public static final DeferredRegister<Skill> SKILLS = DeferredRegister.create(new ResourceLocation(EpicFightMod.MODID, "skill"), KingdomKeysReMind.MODID);
    //public static final RegistryObject<Skill> renewalBlock = SKILLS.register("renewal_block", () -> new BlockAbilities(GuardSkill.createGuardBuilder()));
    //public static final RegistryObject<Skill> focusBlock = SKILLS.register("focus_block", () -> new BlockAbilities(GuardSkill.createGuardBuilder()));
}