## Description
This mod fixes the game camera when the **scale attribute** is modified (mainly at small sizes, as no issues or inconveniences have been observed at larger sizes).
The list of fixes and changes includes:
- **Fixed camera clipping into blocks at small sizes**
- **Reduced camera sway when View Bobbing is enabled at small sizes**
## Camera Cliping
![[./readme-assets/camera-cliping.gif]]
Removes camera clipping by adjusting the near plane closer to the camera center depending on the size.
## View Bobbing
![[./readme-assets/view-bobbing.gif]]
Reduces camera sway depending on the size. Also affects hand shaking intensity (configuration may be added in the future).