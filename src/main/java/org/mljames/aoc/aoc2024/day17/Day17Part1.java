package org.mljames.aoc.aoc2024.day17;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day17Part1
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day17Part1.class);

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final Register register = new Register(30344604, 0, 0);

        final InstructionPointer pointer = new InstructionPointer();
        final List<Integer> program = List.of(2,4,1,1,7,5,1,5,4,5,0,3,5,5,3,0);

        final List<Integer> out = new ArrayList<>();
        while (pointer.pointer < program.size() - 1)
        {
            final Instruction instructionOpcode = Instruction.fromValue(program.get(pointer.pointer++));
            final Operand operand = Operand.fromValue(program.get(pointer.pointer++));

            instructionOpcode.operate(operand, register, pointer, out);
        }

        LOGGER.info("After the program has finished running it's output is: [{}], calculated in {}ms.",
                out.stream().map(String::valueOf).collect(Collectors.joining(",")),
                System.currentTimeMillis() - start);
    }

    private static final class Register
    {
        long registerA;
        long registerB;
        long registerC;

        public Register(final long registerA, final long registerB, final long registerC)
        {
            this.registerA = registerA;
            this.registerB = registerB;
            this.registerC = registerC;
        }
    }

    private static final class InstructionPointer
    {
        int pointer = 0;
    }

    private enum Operand
    {
        ZERO(0),
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7);

        private final int literalOperand;

        Operand(final int literalOperand)
        {
            this.literalOperand = literalOperand;
        }

        private static Operand fromValue(final int value)
        {
            for (final Operand operand : values())
            {
                if (operand.literalOperand == value)
                {
                    return operand;
                }
            }
            throw new RuntimeException("Unrecognised value!!");
        }

        private long getComboOperand(final Register register)
        {
            return switch (this)
            {
                case ZERO, TWO, ONE, THREE -> this.literalOperand;
                case FOUR -> register.registerA;
                case FIVE -> register.registerB;
                case SIX -> register.registerC;
                case SEVEN -> throw new RuntimeException("Invalid operand!!");
            };
        }
    }

    private enum Instruction
    {
        ZERO(0),
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7);

        private final int value;

        Instruction(final int value)
        {
            this.value = value;
        }

        private static Instruction fromValue(final int value)
        {
            for (final Instruction instruction : values())
            {
                if (instruction.value == value)
                {
                    return instruction;
                }
            }
            throw new RuntimeException("Unrecognised value!!");
        }

        private void operate(
                final Operand operand,
                final Register register,
                final InstructionPointer pointer,
                final List<Integer> out)
        {
            switch (this)
            {
                case ZERO -> register.registerA = (long) (register.registerA / Math.pow(2, operand.getComboOperand(register)));
                case ONE -> register.registerB = register.registerB ^ operand.literalOperand;
                case TWO -> register.registerB = operand.getComboOperand(register) % 8;
                case THREE ->
                {
                    if (register.registerA != 0)
                    {
                        pointer.pointer = operand.literalOperand;
                    }
                }
                case FOUR -> register.registerB = register.registerB ^ register.registerC;
                case FIVE -> out.add((int) (operand.getComboOperand(register) % 8));
                case SIX -> register.registerB = (long) (register.registerA / Math.pow(2, operand.getComboOperand(register)));
                case SEVEN -> register.registerC = (long) (register.registerA / Math.pow(2, operand.getComboOperand(register)));
            }
        }
    }
}
