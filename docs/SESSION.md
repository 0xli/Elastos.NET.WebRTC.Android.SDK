# Session memory (read me)

Last updated: 2026-08-29

## Current pins

- BeagleChat ships **`org.elastos.carrier:webrtc:0.0.4-beagle6`** from local-maven.
- Native WebRTC: **`io.github.webrtc-sdk:android:137.7151.03`** (16 KB aligned). Old `org.webrtc:google-webrtc:1.0.30039` retired for Play 16 KB.
- `RTCConfiguration.enableDtlsSrtp` removed (always on) — fixed in `CarrierPeerConnectionClient`.

## Do / Don’t

- After API/native changes: `./gradlew :webrtc:assembleRelease` then install AAR into beaglechat `local-maven` as a new `-beagleN` version.

## Open follow-ups

- Upstream `publishToBeagleLocal` still documents old google-webrtc POM — update when next publishing.
