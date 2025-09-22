import React, { useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Calculator } from './Calculator';

type NumberSystem = 'DEC' | 'BIN' | 'OCT' | 'HEX';

interface CalculationResult {
  decimal: number;
  binary: string;
  octal: string;
  hex: string;
}

interface CalculationStep {
  operation: string;
  operand1: string;
  operand2: string;
  system: NumberSystem;
  decimalOperand1: number;
  decimalOperand2: number;
  decimalResult: number;
  explanation: string;
}

export const CalculationMode = () => {
  const [display, setDisplay] = useState('0');
  const [previousValue, setPreviousValue] = useState<number | null>(null);
  const [operation, setOperation] = useState<string | null>(null);
  const [currentSystem, setCurrentSystem] = useState<NumberSystem>('DEC');
  const [result, setResult] = useState<CalculationResult | null>(null);
  const [calculationStep, setCalculationStep] = useState<CalculationStep | null>(null);

  const convertToDecimal = (value: string, system: NumberSystem): number => {
    switch (system) {
      case 'DEC': return parseInt(value) || 0;
      case 'BIN': return parseInt(value, 2) || 0;
      case 'OCT': return parseInt(value, 8) || 0;
      case 'HEX': return parseInt(value, 16) || 0;
      default: return 0;
    }
  };

  const convertFromDecimal = (value: number): CalculationResult => {
    return {
      decimal: value,
      binary: value.toString(2),
      octal: value.toString(8),
      hex: value.toString(16).toUpperCase()
    };
  };

  const isValidDigit = (digit: string, system: NumberSystem): boolean => {
    switch (system) {
      case 'DEC': return /^[0-9]$/.test(digit);
      case 'BIN': return /^[01]$/.test(digit);
      case 'OCT': return /^[0-7]$/.test(digit);
      case 'HEX': return /^[0-9A-F]$/.test(digit);
      default: return false;
    }
  };

  const handleKeypadInput = (value: string) => {
    if (value === 'C') {
      setDisplay('0');
      setPreviousValue(null);
      setOperation(null);
      setResult(null);
      setCalculationStep(null);
    } else if (value === '⌫') {
      setDisplay(prev => prev.length > 1 ? prev.slice(0, -1) : '0');
    } else if (['+', '-'].includes(value)) {
      const currentValue = convertToDecimal(display, currentSystem);
      setPreviousValue(currentValue);
      setOperation(value);
      setDisplay('0');
    } else if (value === '=') {
      if (previousValue !== null && operation) {
        const currentValue = convertToDecimal(display, currentSystem);
        let resultValue = 0;
        
        // Store original values in current system
        const operand1Converted = convertFromDecimal(previousValue);
        const operand1InSystem = currentSystem === 'DEC' ? operand1Converted.decimal.toString() : 
                                currentSystem === 'BIN' ? operand1Converted.binary :
                                currentSystem === 'OCT' ? operand1Converted.octal :
                                operand1Converted.hex;
        const operand2InSystem = display;
        
        switch (operation) {
          case '+':
            resultValue = previousValue + currentValue;
            break;
          case '-':
            resultValue = previousValue - currentValue;
            break;
        }
        
        // Get result in current system
        const resultConverted = convertFromDecimal(resultValue);
        const resultInSystem = currentSystem === 'DEC' ? resultConverted.decimal.toString() : 
                              currentSystem === 'BIN' ? resultConverted.binary :
                              currentSystem === 'OCT' ? resultConverted.octal :
                              resultConverted.hex;
        
        // Create step-by-step explanation
        const step: CalculationStep = {
          operation,
          operand1: operand1InSystem,
          operand2: operand2InSystem,
          system: currentSystem,
          decimalOperand1: previousValue,
          decimalOperand2: currentValue,
          decimalResult: resultValue,
          explanation: `${operand1InSystem} ${operation} ${operand2InSystem} = ${resultInSystem} (in ${currentSystem})`
        };
        
        const calculationResult = convertFromDecimal(resultValue);
        setResult(calculationResult);
        setCalculationStep(step);
        setDisplay(resultInSystem);
        setPreviousValue(null);
        setOperation(null);
      }
    } else if (isValidDigit(value, currentSystem)) {
      setDisplay(prev => prev === '0' ? value : prev + value);
    }
  };

  const handleSystemChange = (system: NumberSystem) => {
    // Convert current display to new system
    const decimalValue = convertToDecimal(display, currentSystem);
    setCurrentSystem(system);
    
    switch (system) {
      case 'DEC':
        setDisplay(decimalValue.toString());
        break;
      case 'BIN':
        setDisplay(decimalValue.toString(2));
        break;
      case 'OCT':
        setDisplay(decimalValue.toString(8));
        break;
      case 'HEX':
        setDisplay(decimalValue.toString(16).toUpperCase());
        break;
    }
  };

  return (
    <div className="space-y-4">
      {/* Number System Mode Selector */}
      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Number System</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-4 gap-2">
            {(['DEC', 'BIN', 'OCT', 'HEX'] as NumberSystem[]).map((system) => (
              <Button
                key={system}
                variant={currentSystem === system ? 'default' : 'outline'}
                onClick={() => handleSystemChange(system)}
                className={currentSystem === system ? 'bg-primary text-primary-foreground' : ''}
              >
                {system}
              </Button>
            ))}
          </div>
        </CardContent>
      </Card>

      {/* Calculator Display */}
      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Calculator ({currentSystem})</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="bg-calculator-display text-calculator-display-text p-4 rounded-lg text-right text-2xl font-mono mb-4">
            {display}
            {operation && <div className="text-sm text-accent">Operation: {operation}</div>}
          </div>
          
          {/* Current Calculation Display */}
          {(previousValue !== null || operation) && (
            <div className="bg-muted p-3 rounded-lg text-sm">
              <div className="font-semibold text-muted-foreground mb-2">Current Calculation:</div>
              <div className="font-mono">
                {previousValue !== null && (
                  <span>
                    {convertFromDecimal(previousValue)[currentSystem.toLowerCase() as keyof CalculationResult]}
                    {operation && <span className="text-accent mx-2">{operation}</span>}
                    {operation && display !== '0' && (
                      <span>{display}</span>
                    )}
                  </span>
                )}
              </div>
            </div>
          )}
        </CardContent>
      </Card>

      {/* Step-by-Step Calculation */}
      {calculationStep && (
        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Step-by-Step Calculation</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              <div className="bg-muted p-3 rounded-lg">
                <div className="font-semibold text-sm text-muted-foreground mb-2">Operation in {calculationStep.system}:</div>
                <div className="font-mono text-lg">{calculationStep.explanation}</div>
              </div>
              
              <div className="bg-muted p-3 rounded-lg">
                <div className="font-semibold text-sm text-muted-foreground mb-2">Conversion to Decimal:</div>
                <div className="space-y-1 text-sm">
                  <div className="font-mono">
                    {calculationStep.operand1} ({calculationStep.system}) = {calculationStep.decimalOperand1} (DEC)
                  </div>
                  <div className="font-mono">
                    {calculationStep.operand2} ({calculationStep.system}) = {calculationStep.decimalOperand2} (DEC)
                  </div>
                </div>
              </div>
              
              <div className="bg-muted p-3 rounded-lg">
                <div className="font-semibold text-sm text-muted-foreground mb-2">Decimal Calculation:</div>
                <div className="font-mono text-lg">
                  {calculationStep.decimalOperand1} {calculationStep.operation} {calculationStep.decimalOperand2} = {calculationStep.decimalResult}
                </div>
              </div>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Results Display */}
      {result && (
        <Card>
          <CardHeader>
            <CardTitle className="text-lg">All Number Systems</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-1 gap-3">
              <div className="bg-muted p-3 rounded-lg">
                <div className="font-semibold text-sm text-muted-foreground">Decimal (DEC)</div>
                <div className="font-mono text-lg">{result.decimal}</div>
              </div>
              <div className="bg-muted p-3 rounded-lg">
                <div className="font-semibold text-sm text-muted-foreground">Binary (BIN)</div>
                <div className="font-mono text-lg">{result.binary}</div>
              </div>
              <div className="bg-muted p-3 rounded-lg">
                <div className="font-semibold text-sm text-muted-foreground">Octal (OCT)</div>
                <div className="font-mono text-lg">{result.octal}</div>
              </div>
              <div className="bg-muted p-3 rounded-lg">
                <div className="font-semibold text-sm text-muted-foreground">Hexadecimal (HEX)</div>
                <div className="font-mono text-lg">{result.hex}</div>
              </div>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Calculator Keypad */}
      <Calculator 
        onInput={handleKeypadInput} 
        showOperators={true}
        currentSystem={currentSystem}
      />
    </div>
  );
};
