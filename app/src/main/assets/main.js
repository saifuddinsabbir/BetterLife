//timeline
const tl = gsap.timeline();

tl.from(".main-container .wrapper1",{
    y:200,
    duration:5,
    opacity: 0,
    ease: Elastic.easeOut
}).from(".main-container .wrapper2",{
  x:200,
  duration:5,
  opacity: 0,
  ease: Elastic.easeOut
}, "-=5");
