import React, { useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Calculator } from './Calculator';

interface ConversionSteps {
  decimal: number;
  binary: { value: string; steps: string[] };
  octal: { value: string; steps: string[] };
  hex: { value: string; steps: string[] };
}

export const ConversionMode = () => {
  const [input, setInput] = useState('');
  const [results, setResults] = useState<ConversionSteps | null>(null);

  const convertDecimalToBinary = (num: number): { value: string; steps: string[] } => {
    if (num === 0) return { value: '0', steps: ['0 ÷ 2 = 0 remainder 0'] };
    
    const steps: string[] = [];
    let temp = num;
    let binary = '';
    
    while (temp > 0) {
      const remainder = temp % 2;
      steps.push(`${temp} ÷ 2 = ${Math.floor(temp / 2)} remainder ${remainder}`);
      binary = remainder + binary;
      temp = Math.floor(temp / 2);
    }
    
    steps.push(`Reading remainders from bottom to top: ${binary}`);
    return { value: binary, steps };
  };

  const convertDecimalToOctal = (num: number): { value: string; steps: string[] } => {
    if (num === 0) return { value: '0', steps: ['0 ÷ 8 = 0 remainder 0'] };
    
    const steps: string[] = [];
    let temp = num;
    let octal = '';
    
    while (temp > 0) {
      const remainder = temp % 8;
      steps.push(`${temp} ÷ 8 = ${Math.floor(temp / 8)} remainder ${remainder}`);
      octal = remainder + octal;
      temp = Math.floor(temp / 8);
    }
    
    steps.push(`Reading remainders from bottom to top: ${octal}`);
    return { value: octal, steps };
  };

  const convertDecimalToHex = (num: number): { value: string; steps: string[] } => {
    if (num === 0) return { value: '0', steps: ['0 ÷ 16 = 0 remainder 0'] };
    
    const steps: string[] = [];
    let temp = num;
    let hex = '';
    const hexDigits = '0123456789ABCDEF';
    
    while (temp > 0) {
      const remainder = temp % 16;
      const hexDigit = hexDigits[remainder];
      steps.push(`${temp} ÷ 16 = ${Math.floor(temp / 16)} remainder ${remainder} (${hexDigit})`);
      hex = hexDigit + hex;
      temp = Math.floor(temp / 16);
    }
    
    steps.push(`Reading remainders from bottom to top: ${hex}`);
    return { value: hex, steps };
  };

  const handleConvert = () => {
    const num = parseInt(input);
    if (isNaN(num) || num < 0) return;

    const conversion: ConversionSteps = {
      decimal: num,
      binary: convertDecimalToBinary(num),
      octal: convertDecimalToOctal(num),
      hex: convertDecimalToHex(num)
    };

    setResults(conversion);
  };

  const handleKeypadInput = (value: string) => {
    if (value === 'C') {
      setInput('');
      setResults(null);
    } else if (value === '⌫') {
      setInput(prev => prev.slice(0, -1));
    } else {
      setInput(prev => prev + value);
    }
  };

  return (
    <div className="space-y-4">
      {/* Input Display */}
      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Decimal Input</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="bg-calculator-display text-calculator-display-text p-4 rounded-lg text-right text-2xl font-mono mb-4">
            {input || '0'}
          </div>
          <Button 
            onClick={handleConvert} 
            className="w-full bg-primary hover:bg-primary-hover text-primary-foreground"
            disabled={!input}
          >
            Convert
          </Button>
        </CardContent>
      </Card>

      {/* Results */}
      {results && (
        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Conversion Results</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="grid grid-cols-1 gap-3">
              <div className="bg-muted p-3 rounded-lg">
                <div className="font-semibold text-sm text-muted-foreground">Binary</div>
                <div className="font-mono text-lg">{results.binary.value}</div>
              </div>
              <div className="bg-muted p-3 rounded-lg">
                <div className="font-semibold text-sm text-muted-foreground">Octal</div>
                <div className="font-mono text-lg">{results.octal.value}</div>
              </div>
              <div className="bg-muted p-3 rounded-lg">
                <div className="font-semibold text-sm text-muted-foreground">Hexadecimal</div>
                <div className="font-mono text-lg">{results.hex.value}</div>
              </div>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Step-by-step explanation */}
      {results && (
        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Step-by-Step Conversion</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div>
              <h4 className="font-semibold mb-2 text-primary">Binary Conversion:</h4>
              <div className="space-y-1 text-sm font-mono">
                {results.binary.steps.map((step, index) => (
                  <div key={index} className="text-muted-foreground">{step}</div>
                ))}
              </div>
            </div>
            <div>
              <h4 className="font-semibold mb-2 text-primary">Octal Conversion:</h4>
              <div className="space-y-1 text-sm font-mono">
                {results.octal.steps.map((step, index) => (
                  <div key={index} className="text-muted-foreground">{step}</div>
                ))}
              </div>
            </div>
            <div>
              <h4 className="font-semibold mb-2 text-primary">Hexadecimal Conversion:</h4>
              <div className="space-y-1 text-sm font-mono">
                {results.hex.steps.map((step, index) => (
                  <div key={index} className="text-muted-foreground">{step}</div>
                ))}
              </div>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Calculator Keypad */}
      <Calculator onInput={handleKeypadInput} showOperators={false} />
    </div>
  );
};
