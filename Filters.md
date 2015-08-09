# Introduction #
Each filter exists to process audio, with the exception of the OSC Pass-through filter, which has its own page [here](OSCPassThrough.md).

# Filters #

### Bandpass ###

A bandpass filter passes a narrow band of audio around its cutoff frequency, which is specified by user touch input.

### Bandstop ###

A bandstop silences a narrow band of audio around its cutoff frequency, which is specified by user touch input.

### Bass Wobbler ###

The Bass Wobbler applies dubstep-style wobble to low frequency audio by modulating the cutoff frequency of a [lowpass](Filters#Lowpass.md) filter between 10 and 220 hz on a triangle wave. The speed of the modulation is specified by user touch input.

### Highpass ###

A Highpass filter passes audio with frequency higher than its cutoff frequency, which is specified by user touch input.

### Lowpass ###

A lowpass filter passes audio with frequency lower than its cutoff frequency, which is specified by user touch input.

### Tremelo ###

The tremolo filter modulates the [volume](Filters#Volume.md) of the audio between 0db and parity on a sine wave. The speed of the modulation is specified by user touch input.

### Volume ###

The volume filter manipulates the volume of the audio between 0db and 1.275 times parity (light amplification), with the level specified by user touch input.