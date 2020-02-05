---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by mike.
--- DateTime: 10/20/19 10:35 PM
---

local RPD  = require "scripts/lib/commonClasses"

local buff = require "scripts/lib/buff"

local shields = require "scripts/lib/shields"

return buff.init{
    icon = function(self, buff)
        if self.data.state then
            return 47
        end
        return 48
    end,

    name = function(self, buff)
        if self.data.state then
            return "ShieldBuffReady_Name"
        end
        return "ShieldBuffNotReady_Name"
    end,

    attachTo = function(self, buff, target)
        self.data.state = self.data.state or false
        return true
    end,

    act = function(self,buff)
        if not self.data.state then
            self.data.state = true
            RPD.BuffIndicator:refreshHero()
        end

        buff:spend(shields.rechargeTime(buff:level(),buff.target:effectiveSTR()))
    end,

    defenceProc = function(self, buff, enemy, damage)
        if self.data.state then -- shield was ready
            local lvl = buff:level()

            if math.random() < shields.blockChance(lvl, buff.target:effectiveSTR()) then
                RPD.topEffect(buff.target:getPos(),"shield_blocked")

                RPD.playSound("body_armor")

                self.data.state = false
                RPD.BuffIndicator:refreshHero()

                buff.target:spend(
                        shields.waitAfterBlockTime(
                                buff:level(),
                                buff.target:effectiveSTR()))

                return math.max(damage - shields.blockDamage(lvl,buff:getSource():level()), 0)
            else
                RPD.topEffect(buff.target:getPos(),"shield_broken")
                RPD.playSound("snd_shatter")
            end
        end
        return damage
    end
}
