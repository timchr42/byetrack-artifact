# README

This is the artifact of the bachelor thesis "Byetrack: Capabilities as a Solution against Tracking Across Android Apps" featuring a defense mechanism against HyTrack, a new mobile tracking technique.

## Structure

- `demo`: Contains a demo video of the mitigation in action, as well as a description of the demo.
- `PoC`: Contains the proof-of-concept implementation of the HyTrack webapp, their two android apps equipped with the defense mechanism (with and without policy), a test app for more insight, a malicious app (Evil) trying to impersonate the test app (includes a respective local evil library), the custom installer and a patch file of our Android 15 SDK and Firefox Fenix modifications.

The [library](https://github.com/timchr42/byetrack) which implements the defense mechanism can be found extern on github, such as the modified androidx.browser [library](https://github.com/timchr42/AndroidxBrowserByetrack), demonstating how it can be hooked into the existing android ecosystem.
The source code of the modified [firefox](https://github.com/timchr42/firefox) can be found on github as well.

## Further Information
Refer to the READMEs in the respective subfolders.
