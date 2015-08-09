# Introduction #

OSC Pass-through is a selectable [filter](Filters.md).

The OSC Pass-through filter is a misnomer, in that it does not filter audio; rather it passes it unmolested. What the OSC Pass-through filter does do is send OSC messages.


# Method #

The OSC pass-through filter sends OSC messages using the UDP/IP protocol. The IP address to send to can be specified in the user interface, as well as the port to send to. Each message contains a filter UID (0, indicating the OSC Pass-through filter in question was applied to phone's X axis, and 1 for the Y axis) and a scalar value between -1 and -256, representing the "value" of the filter at present. -1 is "low" (indicating the left/bottom) and -256 is "high", indicating right/top).

The exact format of each OSC message sent is as follows:

```
     Address: /fermataCoordinates
     Arguments: {<FilterID>, -1 * <value>}
```

To make parsing easier, the value is always negative, and the UID is always positive; in this fashion there is no overlap between their ranges so no confusion can result.