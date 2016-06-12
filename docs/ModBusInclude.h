//-------------------------------------------------------------------------------------------------
/*! \file ModBudInclude.h
 *
 *  \brief Header file containing the registers for the WattNode energy meter
 *
 *  \author Neels Kruger
 *  \date 2016 04  */
//-------------------------------------------------------------------------------------------------

// This type define the Basic Register List of the WattNode
// The registers start at 1200.
typedef struct
{
  // Energy Registers
  int32_t s32EnergySum;       // 0.1kWh Total net (bi-directional) energy
  int32_t s32EnergyPosSum;    // 0.1kWh Total positive energy
  int32_t s32EnergySumNR;     // 0.1kWh Total net (bi-directional) energy non-resettable
  int32_t s32EnergyPosSumNR;  // 0.1kWh Total positve energy non-resettable
                              
    // Power Registers
  int16_t s16PowerSum;        // PowerIntScale x Real power, sum of active phases
  int16_t s16PowerA;          // PowerIntScale x Real power, phase A
  int16_t s16PowerB;          // PowerIntScale x Real power, phase B
  int16_t s16PowerC;          // PowerIntScale x Real power, phase C

  // Voltage Registers
  int16_t s16VoltAvgLN;       // 0.1V Average phase-to-neutral voltage   
  int16_t s16VoltA;           // 0.1V RMS voltage, phase A to neutral
  int16_t s16VoltB;           // 0.1V RMS voltage, phase B to neutral
  int16_t s16VoltC;           // 0.1V RMS voltage, phase C to neutral
  int16_t s16VoltAvgLL;       // 0.1V Average line-to-line voltage
  int16_t s16VoltAB;          // 0.1V RMS voltage, line-to-line, phase A to B
  int16_t s16VoltBC;          // 0.1V RMS voltage, line-to-line, phase B to C
  int16_t s16VoltAC;          // 0.1V RMS voltage, line-to-line, phase A to C
                               
  // Frequency Register
  int16_t s16Freq;            // 0.1Hz Power line frequency
} CBasicRegisterlist_t;

// This type define the Current Registers available from the Advance Register List
// These three registers start at register 1350
typedef struct
{
  int16_t s16CurrentA;         // CurrentintScale Current, phase A
  int16_t s16CurrentB;         // CurrentintScale Current, phase B
  int16_t s16CurrentC;         // CurrentintScale Current, phase C
} CAdvancedCurrent_t;

// This type define the Power Factor Registers available from the Advance Register List
// These three registers start at register 1340
typedef struct
{
  int16_t s16PowerFactorA;     // 0.1kWh Power factor, phase A
  int16_t s16PowerFactorB;     // 0.1kWh Power factor, phase B
  int16_t s16PowerFactorC;     // 0.1kWh Power factor, phase C
} CAdvancedPowerFactor_t;

// This type define the Positive Energy Registers available from the Advance Register List
// These three registers start at register 1306
typedef struct
{
  int32_t s32EnergyPosA;       // 0.1kWh Positive Energy, phase A
  int32_t s32EnergyPosB;       // 0.1kWh Positive Energy, phase B
  int32_t s32EnergyPosC;       // 0.1kWh Positive Energy, phase C
} CAdvancedEnergy_t;

// This type define the Configuration Register List of the WattNode
// The registers start at 1600.
typedef struct
{
  int32_t s32ConfigPassCode;       // Optional passcode to prevent unauthorized changes to configuration
  int16_t s16CtAmps;               // 1A 5 Assign global current transformer rated current
  int16_t s16CtAmpsA;              // 1A 5 �A CT rated current (0 to 6000)
  int16_t s16CtAmpsB;              // 1A 5 �B CT rated current (0 to 6000)
  int16_t s16CtAmpsC;              // 1A 5 �C CT rated current (0 to 6000)
  int16_t s16CtDirections;         // 0 Optionally invert CT orientations (0 to 7)
  int16_t s16Averaging;            // 1 (fast) Configure measurement averaging (0 to 3)
  int16_t s16PowerIntScale;        // 1W 0 (auto) Scaling for integer power registers (0 to 1000)
  int16_t s16DemPerMins;           // 1min 15 Demand period (1 to 720)
  int16_t s16DemSubints;           // 1 Number of demand subintervals (1 to 10)
  int16_t s16GainAdjustA;          // 1/10000th 10000 �A power/energy adjustment (5000 to 20000)
  int16_t s16GainAdjustB;          // 1/10000th 10000 �B power/energy adjustment (5000 to 20000)
  int16_t s16GainAdjustC;          // 1/10000th 10000 �C power/energy adjustment (5000 to 20000)
  int16_t s16PhaseAdjustA;         // 0.001 deg -1000 �A CT phase angle adjust (-8000 to 8000)
  int16_t s16PhaseAdjustB;         // 0.001 deg -1000 �B CT phase angle adjust (-8000 to 8000)
  int16_t s16PhaseAdjustC;         // 0.001 deg -1000 �C CT phase angle adjust (-8000 to 8000)
  int16_t s16CreepLimit;           // 1500 Minimum power for readings (100 to 10000)
  int16_t s16PhaseOffset;          // 1 degree 120 Nominal angle between primary voltage phases 
                                   // (0, 60, 90, 120 or 180)
  int16_t s16ZeroEnergy;           // 0 Write 1 to zero all resettable energy registers
  int16_t s16ZeroDemand;           // 0 Write 1 to zero all demand values
  int16_t s16CurrentIntScale;      // 20000 Scale factor for integer currents (0 to 32767)
  int16_t s16IoPinMode;            // varies I/O pin mode for Option IO or SSR (0 to 8)
} CConfiguration_t;

// This structure concatenate all registers sequentially
typedef struct
{
  CBasicRegisterlist_t   CBasicReg;
  CAdvancedCurrent_t     CAdvCurrent;
  CAdvancedPowerFactor_t CAdvPF;
  CAdvancedEnergy_t      CAdvEnergy;
  CConfiguration_t       CConf;
} CAllRegisters_t;

// This is the easiest way to create new packets.
// TOTAL_NO_OF_PACKETS is automatically updated.
enum
{
  PACKET1,
  PACKET2,
  PACKET3,
  PACKET4,
  PACKET5,
  TOTAL_NO_OF_PACKETS // leave this last entry
};

// __ Variables _________________________________________________________________________
Packet packets[TOTAL_NO_OF_PACKETS];             //!< Array of Packets to be configured

// Data read from the energy meter will be stored in these arrays
// unsigned int au16BasicRegs[21];
// unsigned int au16AdvCurrentRegs[3];
// unsigned int au16AdvPwrFactRegs[3];
// unsigned int au16AdvEnergyRegs[6]; 
// unsigned int au16ConfigRegs[23];

/*! 
  \union CAllReg 
  \brief Registers read from the energy meter will be stored in this structure/array
*/
union
{
  CAllRegisters_t CRegs;
  uint16_t        u16Regs[sizeof(CAllRegisters_t)];  // Total of 56 int16 registers.
} CAllReg;

//unsigned int         au16BasicRegs[21];
//TCBasicRegisterlist  *pCBasicRegister = (TCBasicRegisterlist*)  au16BasicRegs;  // ToDo: Union with TCBasicRegisterlist
