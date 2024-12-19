package org.mljames.aoc.aoc2024.day17;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Day17Part2
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Day17Part2.class);

    public static void main(String[] args)
    {
        final long start = System.currentTimeMillis();

        final List<Long> program = List.of(2L,4L,1L,1L,7L,5L,1L,5L,4L,5L,0L,3L,5L,5L,3L,0L);

        final Registers registers = new Registers(0, 0);

        final InstructionPointer pointer = new InstructionPointer();

        long registerAValue = 0;
        final List<Operation> operations = new ArrayList<>();
        while (pointer.pointer < program.size() - 1)
        {
            final Instruction instructionOpcode = Instruction.fromValue(program.get(pointer.pointer++));
            final Operand operand = Operand.fromValue(program.get(pointer.pointer++));

            final Operation operation = instructionOpcode.operate(operand, registers);
            operations.add(operation);
        }

        LOGGER.info("The lowest value for register A for the program to output itself is {}, calculated in {}ms.",
                registerAValue,
                System.currentTimeMillis() - start);
    }

    private void operate(
            final Operation operation,
            final Registers registers,
            final long initialRegisterAValue,
            final List<Long> out,
            final InstructionPointer pointer)
    {
        switch (operation.action)
        {
            case SET_REGISTER_A -> registers.registerA = operation.result.apply(initialRegisterAValue);
            case SET_REGISTER_B -> registers.registerB = operation.result.apply(initialRegisterAValue);
            case Action.SET_REGISTER_C -> registers.registerC = operation.result.apply(initialRegisterAValue);
            case OUT -> out.add(initialRegisterAValue);
            case MOVE_POINTER -> pointer.pointer = operation.result.apply(initialRegisterAValue).intValue();
        }
    }

    private static final class Registers
    {
        long registerA;
        long registerB;
        long registerC;

        public Registers(final long registerB, final long registerC)
        {
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

        private final long literalOperand;

        Operand(final int literalOperand)
        {
            this.literalOperand = literalOperand;
        }

        private static Operand fromValue(final long value)
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

        private Function<Long, Long> getComboOperand(final Registers register)
        {
            return switch (this)
            {
                case ZERO, TWO, ONE, THREE -> (registerA) -> this.literalOperand;
                case FOUR -> (registerA) -> registerA;
                case FIVE -> (registerA) -> register.registerB;
                case SIX -> (registerA) -> register.registerC;
                case SEVEN -> throw new RuntimeException("Invalid operand!!");
            };
        }
    }

    private static class Operation
    {
        private final Action action;
        private final Function<Long, Long> result;

        private Operation(final Action action, final Function<Long, Long> result)
        {
            this.action = action;
            this.result = result;
        }
    }

    private enum Action
    {
        SET_REGISTER_A,
        SET_REGISTER_B,
        SET_REGISTER_C,
        MOVE_POINTER,
        OUT;
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

        private final long value;

        Instruction(final long value)
        {
            this.value = value;
        }

        private static Instruction fromValue(final long value)
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

        private Operation operate(
                final Operand operand,
                final Registers registers)
        {
            return switch (this)
            {
                case ZERO -> new Operation(Action.SET_REGISTER_A, (registerAValue) -> (long) (registerAValue / Math.pow(2, operand.getComboOperand(registers).apply(registerAValue))));
                case ONE -> new Operation(Action.SET_REGISTER_B, (registerAValue) -> (registers.registerB ^ operand.literalOperand));
                case TWO -> new Operation(Action.SET_REGISTER_B, (registerAValue) -> operand.getComboOperand(registers).apply(registerAValue) % 8);
                case THREE -> new Operation(Action.MOVE_POINTER, (registerAValue) -> registerAValue != 0 ? operand.literalOperand : 0L);
                case FOUR -> new Operation(Action.SET_REGISTER_B, (registerAValue) -> registers.registerB ^ registers.registerC);
                case FIVE -> new Operation(Action.OUT, (registerAValue) -> operand.getComboOperand(registers).apply(registerAValue) % 8);
                case SIX -> new Operation(Action.SET_REGISTER_B, (registerAValue) -> (long) (registerAValue / Math.pow(2, operand.getComboOperand(registers).apply(registerAValue))));
                case SEVEN -> new Operation(Action.SET_REGISTER_C, (registerAValue) -> (long) (registerAValue / Math.pow(2, operand.getComboOperand(registers).apply(registerAValue))));
            };
        }
    }
}
