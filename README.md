# Frontend Mentor challenge solutions

| Challenge | Solution | Live site |
| --------- | ------------ | ------------- |
| QR code component | [Solution](https://github.com/cellux/frontendmentor-challenges/blob/master/src/io/github/cellux/frontendmentor_challenges/qr_code_component_main/core.clj) | [Live site](https://cellux.github.io/frontendmentor-challenges/qr-code-component-main/) |

## My process

I hacked together a static site generator in Clojure and used that to
generate the static content (HTML/CSS) for the challenges.

HTML is described with a data structure and rendered via
[Hiccup](https://github.com/weavejester/hiccup).

CSS is described with a data structure and rendered via
[Garden](https://github.com/noprompt/garden).

## Files

1. Source code: `src/io/github/cellux/frontendmentor_challenges/ID`
1. Resources: `resources/io/github/cellux/frontendmentor_challenges/ID`

where `ID` is an identifier for a particular challenge, eg. `qr_code_component_main`.

## Usage

Fire up a Clojure REPL and run `(watch)` in the `user` namespace.

Saving any of the challenge files will result in automatic rebuild of
the static content.

Output files will be written to `docs/ID`.

## What I learned

- on Firefox running on Linux Wayland the size of the paragraph font is not `15px` but `15.5px`
- a CSS pixel is not the same as a device pixel; use of the `px` unit seems safe
- an easy way to center something is to add these CSS rules to a top-level container element:
  ```
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  ```
- the original design JPG can be overlaid with my implementation via
  `position: absolute` and `z-index: 1`
- setting `visibility: hidden` on the design JPG hides it by default;
  it can be toggled on/off via DevTools
- `box-sizing: border-box` is very useful
