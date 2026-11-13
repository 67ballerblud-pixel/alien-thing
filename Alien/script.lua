-- Alien avatar: hides your normal player model and squishes the blocky alien into the video proportions.
vanilla_model.PLAYER:setVisible(false)

local alien = models.model
alien.Head:setScale(1.7, 1.7, 1.7)          -- big head
alien.Body:setScale(0.85, 1, 0.7)           -- thin torso
alien.LeftArm:setScale(0.65, 1.3, 0.65)     -- long skinny arms
alien.RightArm:setScale(0.65, 1.3, 0.65)
alien.LeftLeg:setScale(0.75, 1, 0.75)       -- thin legs
alien.RightLeg:setScale(0.75, 1, 0.75)

nameplate.ENTITY:setPivot(0, 2.45, 0)       -- lift the name tag above the big head

-- slow creepy head tilt
function events.RENDER(delta, ctx)
  local t = world.getTime(delta)
  alien.Head:setOffsetRot(math.sin(t * 0.07) * 3, 0, math.sin(t * 0.045) * 4)
end
