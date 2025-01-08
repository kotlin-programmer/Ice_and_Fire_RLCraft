package com.github.alexthe666.iceandfire.asm;

import java.util.NoSuchElementException;
import java.util.function.Predicate;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class IceAndFireClassTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String obfName, String name, byte[] basicClass) {
        if (name.equals("net.minecraft.block.state.BlockStateContainer$StateImplementation")) {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(basicClass);
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            classReader.accept(classNode, ClassReader.SKIP_FRAMES);

            MethodNode method = findMethod(classNode, "addCollisionBoxToList", "a", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;Z)V", "(Lamu;Let;Lbhb;Ljava/util/List;Lvg;Z)V");
            InsnList list = new InsnList();
            // if (entityIn instanceof IPhasesThroughBlock && ((IPhasesThroughBlock) entityIn).canPhaseThroughBlock(this, worldIn, pos)) return;
            list.add(new VarInsnNode(Opcodes.ALOAD, 5));
            list.add(new TypeInsnNode(Opcodes.INSTANCEOF, "com/github/alexthe666/iceandfire/entity/util/IPhasesThroughBlock"));
            list.add(new JumpInsnNode(Opcodes.IFEQ, findFirstInsn(method, LabelNode.class, null)));
            list.add(new VarInsnNode(Opcodes.ALOAD, 5));
            list.add(new TypeInsnNode(Opcodes.CHECKCAST, "com/github/alexthe666/iceandfire/entity/util/IPhasesThroughBlock"));
            list.add(new VarInsnNode(Opcodes.ALOAD, 0));
            list.add(new VarInsnNode(Opcodes.ALOAD, 1));
            list.add(new VarInsnNode(Opcodes.ALOAD, 2));
            list.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "com/github/alexthe666/iceandfire/entity/util/IPhasesThroughBlock", "canPhaseThroughBlock", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", true));
            list.add(new JumpInsnNode(Opcodes.IFNE, findInsn(findLastInsn(method, InsnNode.class, insn -> insn.getOpcode() == Opcodes.RETURN), false, LabelNode.class, null)));
            method.instructions.insert(list);

            classNode.accept(classWriter);
            return classWriter.toByteArray();
        }
        return basicClass;
    }

    static MethodNode findMethod(ClassNode classNode, String name, String obfName, String desc, String obfDesc) {
        for (MethodNode method : classNode.methods) {
            if (!method.name.equals(obfName) && !method.name.equals(name)) {
                continue;
            }
            if (!method.desc.equals(obfDesc) && !method.desc.equals(desc)) {
                continue;
            }
            return method;
        }
        throw new NoSuchElementException();
    }

    static <T extends AbstractInsnNode> T findFirstInsn(MethodNode method, Class<T> type, Predicate<T> predicate) {
        return findInsn(method.instructions.getFirst(), true, type, predicate);
    }

    static <T extends AbstractInsnNode> T findLastInsn(MethodNode method, Class<T> type, Predicate<T> predicate) {
        return findInsn(method.instructions.getLast(), false, type, predicate);
    }

    @SuppressWarnings("unchecked")
    static <T extends AbstractInsnNode> T findInsn(AbstractInsnNode start, boolean searchForward, Class<T> type, Predicate<T> predicate) {
        AbstractInsnNode current = start;
        while (current != null) {
            if ((type == null || type.isInstance(current)) && (predicate == null || predicate.test((T) current))) {
                return (T) current;
            }
            current = searchForward ? current.getNext() : current.getPrevious();
        }
        throw new NoSuchElementException();
    }

}
