import React from 'react';
import { Button } from '@/components/ui/button';
import { Card, CardContent } from '@/components/ui/card';

type NumberSystem = 'DEC' | 'BIN' | 'OCT' | 'HEX';

interface CalculatorProps {
  onInput: (value: string) => void;
  showOperators?: boolean;
  currentSystem?: NumberSystem;
}

export const Calculator: React.FC<CalculatorProps> = ({ 
  onInput, 
  showOperators = false, 
  currentSystem = 'DEC' 
}) => {
  const getAvailableButtons = () => {
    const baseButtons = ['C', '⌫'];
    const numbers = [];
    const operators = showOperators ? ['+', '-', '='] : [];

    // Add available digits based on current system
    switch (currentSystem) {
      case 'BIN':
        numbers.push('0', '1');
        break;
      case 'OCT':
        numbers.push('0', '1', '2', '3', '4', '5', '6', '7');
        break;
      case 'DEC':
        numbers.push('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
        break;
      case 'HEX':
        numbers.push('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F');
        break;
    }

    return { baseButtons, numbers, operators };
  };

  const { baseButtons, numbers, operators } = getAvailableButtons();

  const getButtonStyle = (button: string) => {
    if (button === 'C') {
      return 'bg-calculator-clear hover:bg-red-600 text-white';
    } else if (button === '=') {
      return 'bg-calculator-equals hover:bg-green-600 text-white';
    } else if (['+', '-'].includes(button)) {
      return 'bg-calculator-operator hover:bg-yellow-600 text-white';
    } else if (button === '⌫') {
      return 'bg-calculator-button hover:bg-calculator-button-hover text-calculator-button-text';
    }
    return 'bg-calculator-button hover:bg-calculator-button-hover text-calculator-button-text';
  };

  return (
    <Card>
      <CardContent className="p-4">
        <div className="grid grid-cols-4 gap-2">
          {/* First row: Clear and Backspace */}
          <Button
            onClick={() => onInput('C')}
            className={`h-12 ${getButtonStyle('C')}`}
          >
            C
          </Button>
          <Button
            onClick={() => onInput('⌫')}
            className={`h-12 ${getButtonStyle('⌫')}`}
          >
            ⌫
          </Button>
          <div className="col-span-2"></div>

          {/* Number buttons - arranged in calculator style */}
          {currentSystem === 'HEX' && (
            <>
              {/* Hex letters row */}
              {['A', 'B', 'C', 'D'].map((button) => (
                <Button
                  key={button}
                  onClick={() => onInput(button)}
                  className={`h-12 ${getButtonStyle(button)}`}
                >
                  {button}
                </Button>
              ))}
              {['E', 'F'].map((button) => (
                <Button
                  key={button}
                  onClick={() => onInput(button)}
                  className={`h-12 ${getButtonStyle(button)}`}
                >
                  {button}
                </Button>
              ))}
              <div className="col-span-2"></div>
            </>
          )}

          {/* Numbers 7-9 */}
          {numbers.includes('7') && (
            <Button
              onClick={() => onInput('7')}
              className={`h-12 ${getButtonStyle('7')}`}
            >
              7
            </Button>
          )}
          {numbers.includes('8') && (
            <Button
              onClick={() => onInput('8')}
              className={`h-12 ${getButtonStyle('8')}`}
            >
              8
            </Button>
          )}
          {numbers.includes('9') && (
            <Button
              onClick={() => onInput('9')}
              className={`h-12 ${getButtonStyle('9')}`}
            >
              9
            </Button>
          )}
          {showOperators && (
            <Button
              onClick={() => onInput('+')}
              className={`h-12 ${getButtonStyle('+')}`}
            >
              +
            </Button>
          )}

          {/* Numbers 4-6 */}
          {numbers.includes('4') && (
            <Button
              onClick={() => onInput('4')}
              className={`h-12 ${getButtonStyle('4')}`}
            >
              4
            </Button>
          )}
          {numbers.includes('5') && (
            <Button
              onClick={() => onInput('5')}
              className={`h-12 ${getButtonStyle('5')}`}
            >
              5
            </Button>
          )}
          {numbers.includes('6') && (
            <Button
              onClick={() => onInput('6')}
              className={`h-12 ${getButtonStyle('6')}`}
            >
              6
            </Button>
          )}
          {showOperators && (
            <Button
              onClick={() => onInput('-')}
              className={`h-12 ${getButtonStyle('-')}`}
            >
              -
            </Button>
          )}

          {/* Numbers 1-3 */}
          {numbers.includes('1') && (
            <Button
              onClick={() => onInput('1')}
              className={`h-12 ${getButtonStyle('1')}`}
            >
              1
            </Button>
          )}
          {numbers.includes('2') && (
            <Button
              onClick={() => onInput('2')}
              className={`h-12 ${getButtonStyle('2')}`}
            >
              2
            </Button>
          )}
          {numbers.includes('3') && (
            <Button
              onClick={() => onInput('3')}
              className={`h-12 ${getButtonStyle('3')}`}
            >
              3
            </Button>
          )}
          {showOperators && (
            <Button
              onClick={() => onInput('=')}
              className={`h-12 ${getButtonStyle('=')}`}
            >
              =
            </Button>
          )}

          {/* Number 0 */}
          <Button
            onClick={() => onInput('0')}
            className={`h-12 col-span-2 ${getButtonStyle('0')}`}
          >
            0
          </Button>
          <div className="col-span-2"></div>
        </div>
      </CardContent>
    </Card>
  );
};
