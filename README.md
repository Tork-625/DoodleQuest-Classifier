# DoodleQuest

An Android game where you sketch objects based on fun text hints, and the on-device AI tries to guess what you drew.

DoodleQuest utilizes a lightweight PyTorch-trained model, trained on the **Quick, Draw! dataset** to classify your sketches instantly.

---

## 🎬 Preview

<p align="center">
  <img src="media/1.gif" alt="gameplay-1" width="390"/>
  <img src="media/icon.png" alt="icon" height="184"/>
</p>

<p align="center">
  <img src="media/2.gif" alt="gameplay-2" width="390"/>
  <img src="media/3.gif" alt="gameplay-3" width="390"/>
</p>

## 🎯 Game Flow
1. You receive a short, descriptive hint.
2. You sketch your interpretation on the canvas.
3. The **on-device model** analyzes your sketch and provides **real-time** predictions for the hidden target word.
4. If correct, you unlock a collectible card for your library.
5. If incorrect, you can **refine your sketch** or request a new hint.

The experience loops through over 100 common objects from the [Quick, Draw! dataset](https://quickdraw.withgoogle.com/data).

---

## 🧠 Behind the Scenes
DoodleQuest runs a lightweight PyTorch classifier trained on sketches from the Quick, Draw! dataset. The model runs **fully on-device**, ensuring **instant predictions and a smooth experience**.

---

## 📦 Download
Find the latest Android APK inside the **Releases** section:

1. Download the `.apk` file.
2. Install it on your device.
3. Launch the app and start playing!

---

## Credits / Data Source
This project uses the [Quick, Draw! dataset](https://quickdraw.withgoogle.com/data) by Google.

**Dataset © Google LLC, licensed under [Creative Commons Attribution 4.0 International License](https://creativecommons.org/licenses/by/4.0/).**
