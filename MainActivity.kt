import React, { useState } from 'react';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { ConversionMode } from './ConversionMode';
import { CalculationMode } from './CalculationMode';

const NumberSystemConverter = () => {
  return (
    <div className="min-h-screen bg-background p-4">
      <div className="mx-auto max-w-md">
        <div className="mb-6 text-center">
          <h1 className="text-2xl font-bold text-foreground mb-2">
            Number System Converter
          </h1>
          <p className="text-muted-foreground">
            Convert & Calculate in Binary, Octal, Decimal & Hex
          </p>
        </div>

        <Tabs defaultValue="conversion" className="w-full">
          <TabsList className="grid w-full grid-cols-2 mb-6">
            <TabsTrigger value="conversion">Conversion</TabsTrigger>
            <TabsTrigger value="calculation">Calculator</TabsTrigger>
          </TabsList>

          <TabsContent value="conversion" className="space-y-4">
            <ConversionMode />
          </TabsContent>

          <TabsContent value="calculation" className="space-y-4">
            <CalculationMode />
          </TabsContent>
        </Tabs>
      </div>
    </div>
  );
};

export default NumberSystemConverter; 
