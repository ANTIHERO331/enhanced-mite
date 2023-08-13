## Enhanced MITE

[English](./READMEEN.md)

一个被设计为修复 MITE 1.18.2 的 bug 以及细节问题，并尝试改善 MITE 1.18.2 游玩体验的模组。

### v0.6.2-nst1 中的更改

* 特殊更新版本，包含实验性的更改。
* 为所有MITE添加的矿物添加了粗矿
* 为所有粗矿添加了粗矿粒，且粗矿与粗矿粒之间可以以1: 9的数量互相合成
* 添加了树枝、短木棍，分别延长触及距离0.5和0.25，损坏几率分别为10%和8%；以及，因短木棍太短，所以在使用其攻击敌对生物时仍有几率受到伤害，只是几率更小
* 添加了煤炭碎块，可以由挖掘沙砾得到；且其可作为燃料使用，热值为2
* 现在沙砾不再掉落金属粒，取而代之的是各种粗矿粒
* 粗矿粒可在熔炉中被烧炼为金属粒；热值等级与其粗矿形式相同（除金银铜外，三者比其粗矿低一等级），以避免后期用分解粗矿的方式来降低其热值等级
* 树叶不再直接掉落木棍，而是以更高的几率掉落树枝
* 一根树枝可以合成一根短木棍；四根短木棍可以合成一根木棍
* 添加了更多合成火把的方式
* 现在合成出来的火把是熄灭的；可以使用多种方式点燃它（使用打火石、在另一些火把上使用、在篝火上使用、在熔炉上使用等），一次最多同时处理16根。火把点然后可永久使用。
* 降低了打火石的合成等级（3 -> 2；即只需铜工作台即可合成）
* 修复了上一个版本中，僵尸不持有装备的问题
* 大多数矿石现在有额外的掉落物（如其他的金属，但几率较小）
* 调小了怪物的生成速率
* 看完更新日志的你可能已经晕了。不要担心——这只是一个测试版本，更改不一定会合并到主版本中去（除非非常受欢迎）。作者推荐开新档游玩此版本，一点一点熟悉各种机制；同时请积极给予反馈。以及，模组添加了及其多的新配方，所以建议安装[JEI](https://modrinth.com/mod/jei/version/10.2.1.1002)或[REI](https://modrinth.com/mod/rei/version/8.3.642+fabric)，以方便查询新添加的配方。

注：nst1，New MITE Survival Test v1

### v0.6.1 中的兼容性更改

* 水但又不是很水的更新：再一次提高了模组的兼容性，现在模组中仅存两个由`@Overwrite`注解的注入方法。它们分别为`LandPathNodeMaker$getSuccessors([PathNode;PathNode;)`和`PathNodeNavigatorMixin$findPathToAny(Profiler;PathNode;Map;float;int;float)`。

### v0.6 中的更改

* 作者重构了工作环境，并迅速删除了绝大部分Mixin类中由`@Overwrite`注解的注入方法，取而代之的是其他注入方式。这一改变带来的结果是更高的兼容性，但可能在与部分模组共存时，某些此模组或共存模组的更改失效。
另外，即使大部分`@Overwrite`注入方式被删除，但仍存少量此注解。其可能造成模组之间的不和谐，所以后文有简单的说明以便避坑。
* 使用不对应的工具挖掘方块时不再会损耗工具的耐久
* 现在饥饿的玩家再不能入睡
* 任何工具现在无法加速挖掘功能性方块。已证实：有些本该能空手采集的方块实际上不能空手采集，是因为构造这些方块时未调用`Settings.portable()`。
* 空手不能再采集竹子
* 再次加强了僵尸的AI
* 当床周围的保护足够时，玩家可以忽略其他的睡眠条件入睡（除饥饿、怪物偷家等）
* 修复更新MITE依赖版本后，护甲值错误的问题
* 调整了月亮的大小
* 玩家现在无法拾取在某些条件下的物品——包括但不限于着火、隔着某些不完整方块、看不见它们等
* 添加打火石烧肉这一特性以与1.6.4同步。注：此特性稍微冷门，因此在后文做简单介绍。
* 更改了玩家使用方块的行为，现在需要在正确的侧面上使用才能激活GUI
* 篝火的默认状态现在是熄灭的
* 更改了篝火物品的材质
* 玩家现在可以通过沙砾获取铁粒，几率为1/162
* 纠正了下界合金块的合成时间，现在需12.8675分钟以完成合成
* 修复了各金属块错误的挖掘等级
* 修复了各粗矿块错误的合成配方
* 修复了各工作台错误的合成配方类型
* 修复了此模组的一个离谱bug：僵尸的触及距离受玩家的影响
* 重绘了附加资源包`enhancedmite_textures`中燧石工具的材质
* 加长了白天时间以与1.6.4同步。原白天时间为12000t（一天为24000t），现白天时间为14000t，与原相比增长了2000t（相应地，夜晚时间减少了2000t，所以一天仍为24000t）。同时，日出、日落时间也相应地改动，现在在一天中，日出会在0t（原为0t，未变）时发生；而日落会在14000t（原为12000t）时发生。

#### 附1：可能不兼容的模组所包含的更改

 - 修改了`AbstractBlock$Settings$hardness(float)`。
 - 修改了`ActiveTargetGoal$findClosestTarget()`。
 - 修改了`LandPathNodeMaker$getSuccessors([PathNode;PathNode;)`。
 - 修改了`PathNodeNavigatorMixin$findPathToAny(Profiler;PathNode;Map;float;int;float)`。
 - 修改了`ZombifiedPiglinEntity$initCustomGoals()`。
 - 修改了`BlockItem$place(ItemPlacementContext;BlockState;)`。
 - 修改了`SwordItem$getMiningSpeedMultiplier(ItemStack;BlockState;)`。

#### 附2：使用打火石烹饪生肉

特性实现自MITE 1.6.4，其核心代码如下。

        if (!isFood() || !getFoodComponent().isMeat() || enhancedmite$isCookedMeat(itemEntity.getStack()) || !itemEntity.isOnFire()) return;
        enhancedmite$foodCookingTicks--;
        if (itemEntity.world.getBlockState(itemEntity.getBlockPos()).isOf(Blocks.FIRE)
                && itemEntity.world.random.nextInt(159) == 0)
            itemEntity.world.setBlockState(itemEntity.getBlockPos(), Blocks.AIR.getDefaultState());
        if (enhancedmite$foodCookingTicks <= 0) {
            World world = itemEntity.world;
            Vec3d itemEntityPos = itemEntity.getPos();
            ItemStack stack = itemEntity.getStack();
            int stackCount = stack.getCount();
            Optional<SmeltingRecipe> recipe = world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SimpleInventory(stack), world);
            float cookingExperience = recipe.map(AbstractCookingRecipe::getExperience).orElse(0.0f);
            if (recipe.isPresent()) {
                itemEntity.discard();
                Item result = recipe.get().getOutput().getItem();
                ItemEntity resultEntity;
                world.spawnEntity(new ExperienceOrbEntity(world, itemEntityPos.getX(), itemEntityPos.getY(), itemEntityPos.getZ(), (int) cookingExperience * stackCount));
                world.spawnEntity(resultEntity = new ItemEntity(world, itemEntityPos.getX(), itemEntityPos.getY(), itemEntityPos.getZ(), new ItemStack(result, stackCount)));
                resultEntity.setVelocity(0, 0, 0);
                enhancedmite$foodCookingTicks = recipe.map(AbstractCookingRecipe::getCookTime).orElse(200) * 2 + world.random.nextInt(200);
            }
        }

 - 执行代码的条件，需要掉落物形式的物品满足食物、肉类、非熟肉、着火。所以，使用打火石生火之后，将生肉丢入火中便可触发其余代码。不要担心你的食物会化为灰烬——此情况已在另一个类中避免。
 - 字段 enhancedmite$foodCookingTicks 的值落在区间 [400, 600)。这意味着必须使满足条件的物品着火20秒至30秒才能得到熟肉。
 - 使用此烧炼方式有如下优点：可以批量烧炼，对堆叠了16块肉的处理与仅1块肉的处理是一样的；可以不连续烧炼，即不必时时关注掉落物的着火状态。

### v0.5.1 中的次要更改

* 修复此模组在使用金属币时，如若背包无空位，则不会返还金属粒的问题；现在在以上情况中，返还的金属粒会被丢出 ~~别再怼着岩浆磕了~~
* 修复土元素的一个纹理渲染错误，此错误导致土元素的`isMagma()`方法返回真值时，其眼睛材质透明。错误的产生相当有意思，作者在阅读源代码时认为错误不存在于程序设计；在浏览材质文件时，作者发现是对应的材质中丢失了眼睛部分的像素。
* 纠正了硬化黏土熔炉的名称问题，现在简中名称为 “陶瓦熔炉” ；对应地，其英文名称也纠正为 “Terracotta Furnace” （原为Hardened Clay Furnace）。需要注意：尽管名称被更正，但其id并未变化，仍为`hardened_clay_furnace`。作者认为此细节无伤大雅，所以并未更改，但仍需告知各位玩家。 
* 纠正了石熔炉的名称问题，现在简中名称为 “圆石熔炉”；英文名称未变
* 纠正了沙石熔炉的名称问题，现在简中名称为 “砂岩熔炉”；英文名称未变
* 修改了熔炉烧炼物槽的`canInsert()`判断，现在会为黏土块返回一个真值（以便玩家顺利获取陶瓦熔炉）
* 修改了陶瓦的烧炼配方，现在烧炼粘土块不会提供任何经验
* 修改了陶瓦熔炉的合成配方，现在需要8个陶瓦以合成
* 修改了玻璃的烧炼配方，现在不会给予任何经验（原来为10%几率给予1EXP）
* 删除了砂岩的工作台配方，现在需要使用四个沙子在任意熔炉中使用热值为1的物品烧炼得到
* 添加了砂岩熔炼成玻璃的配方
* 取消注入`MinecraftClient.doItemUse()`中的`Item.isFood()`判断
* 内置了三个资源包。需要注意：资源包并不完善，但不必担心，作者会在后续版本中完善它们。

### v0.5 中的更改

* 为此模组绘制了两个新图标。希望你能喜欢它:D
* 修复了由此模组引起的斧错误的耐久损耗
* 修复了小麦植株错误的掉落物：成熟的小麦植株现在会掉落一个小麦，以及1-2个小麦种子
* 修复了金粒、铁粒错误的合成等级，现在不需要任何工作台便可合成（与1.6.4同步）
* 纠正了使用木棍、骨头时错误的损坏几率，现在分别是2%、1%而不是原来的25%、16.7%（与1.6.4同步）
* 现在玩家触及距离在潜行时会延长0.5格
* 小麦现在可以分解为2个小麦种子
* 玩家现在可以跨上栅栏、石墙。需要注意的是，栅栏门不再会欺骗僵尸的AI，一开一关的门仍可以被僵尸破坏
* 现在使用金属币会返还对应的金属粒 ~~史蒂夫再怎么能吃也不能生吞金属啊，话说铜也不能吃吧~~
* 僵尸不再会攻击骷髅马
* 手持武器的僵尸不再将村民转化为僵尸村民，而是直接杀死
* 猪灵蛮兵现在会手持金战斧或金战锤
* 加强了猪灵蛮兵，以建议玩家不要尝试和它们战斗——而是逃跑
* 僵尸猪灵现在会主动攻击猪灵和疣猪兽，以使猪灵的逃离有意义
* 微调了奖励箱的战利品表
* 强化了僵尸的寻路机制，包括但不限于：它们现在会在三个方向寻路；路径距离最大可以是追踪距离的十倍；路径的跨度被大大增加等（出于性能考虑，路径最长为512个节点长度，为原版的16倍）
* 调整了亡魂的各项属性以与1.6.4同步
* 僵尸及其变种会在血月时获得速度II的效果，同时攻击伤害+2
* 血月当天怪物的挖掘速度增加500% ~~血月现在终于有威胁了~~
* 蓝月当天怪物的挖掘速度减少50%
* 收割之月当天怪物的挖掘速度减少20%
* 更改蜘蛛、木蜘蛛的血量以与1.6.4同步
* 不再兼容于 MITE v0.6.4。使用 MITE v0.6.5 以获得新特性！

温馨提示：怪物现在的寻路方式**不论如何**（在1.6.4则是需满足“**被玩家攻击、拿取工具或者狂暴、一个1/8的概率**”）都会变得**十分刁钻**，“包括但不限于**跨方块索敌、绕路偷袭、空降**等”（引号内容均来自百科），从而导致游戏体验会 ~~十 分 优 秀~~ 。所以游玩时请尽量备份存档和耐心，或考虑卸载这个模组。

### v0.4.1 紧急修复

* 修复注入 RevenantEntity 引起的错误
* 经测试，上个版本有关雷雨时怪物生成的更改不能正常生效，此版本已修复

### v0.4 中的更改

* 怪物在第一次定位玩家时会进行可见性检查 ~~翻译：怪物不再开透视挂了~~
* 怪物一旦~~看上了你~~将你视为目标，就一直会~~看上你~~视你为目标了，除非它们死亡（`isDead()`）或消失（`isRemoved()`）
* 现在玩家在使用物品、破坏方块时会吸引周围的怪物
* 重写了怪物破坏方块的 AI
* 加入了下界合金工具
* 修复亡魂的手持工具，现在亡魂会持有锈铁战斧、锈铁战锤、锈铁剑三者任意
* 修复了雷雨天气会刷新任何怪的问题，现在只会刷新蜘蛛和苦力怕（与1.6.4同步）
* 当怪物持有任意装备时，它们不会被刷新掉
* 两项关于怪物生成的更改：怪物现在只会在亮度为零的地方生成，以及怪物现在会在主世界的任意层数生成

### v0.3.1 中的更改

* 修复了由此模组引起的饱食度消耗的问题
* 修改了更多方块的硬度
* 增加了 `#portable` 标签的内容

### v0.3 中的更改

* 修复了僵尸的触及距离计算，使之与玩家的触及距离完全一致
* 更改奖励箱的战利品表，并且在生成奖励箱时不再生成火把
* 食用食物有20tick（合1秒）间隔

### v0.2 中的更改

* 修复了使用斧和锹时错误的耐久损耗
* 更改了玩家的挖掘计算，现在可以在零饥饿值下仍可以挖掘方块，挖掘速度与1.6.4版本同步
* 修复了僵尸的ZombieBreakBlockGoal.tick()的一些潜在问题，如不渲染破坏粒子、破坏裂纹不正确、僵尸不看向方块
* 使用所有可使用的物品都有8tick（合0.4秒）的间隔

### v0.1 中的更改

* 玩家为零级的时候不会渲染等级文字
* 不使用正确的工具破坏需要工具的方块时不会渲染破坏粒子
* 加入了1.6.4的两个特性：挖掘长草的草方块时会掉落草和草方块的掉落物；将草种在泥土上时会将泥土转化为草方块
