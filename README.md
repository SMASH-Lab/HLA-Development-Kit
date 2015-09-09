# HLA Development Kit

The development of a HLA Federation with its Federates is a quite complex task and there are few training resources for developers [2, 4]. The HLA Development Kit aims at easing the development of HLA Federates by providing the following resources: (i) a _software framework (the DKF)_ for the development in Java of HLA Federates; (ii) a _technical documentation_ that describes the DKF; (iii) a _user guide_ to support developers in the use of the DKF; (iv) a set of _reference examples_ of HLA Federates created by using the DKF; and, (v) _video-tutorials_, which show how to create both the structure and the behavior of a HLA Federate by using the DKF.
The DKF is a general-purpose, domain-independent framework, released under the open source policy Lesser GNU Public License (LGPL), which facilitates the development of HLA Federates. Indeed, the DKF allows developers to focus on the specific aspects of their own Federates rather than dealing with the common HLA functionalities, such as the management of the simulation time; the connection/disconnection on/from the HLA RTI [1, 3]; the publishing, subscribing and updating of ObjectClass and InteractionClass elements. 
The DKF is fully implemented in the Java language and is based on the following three principles: (i) _Interoperability_, DKF is fully compliant with the IEEE 1516-2010 specifications; as a consequence, it is platform-independent and can interoperate with different HLA RTI implementations (e.g. PITCH, VT/MÄK, PoRTIco, CERTI); (ii) _Portability and Uniformity_, DKF provides a homogeneous set of APIs that are independent from the underlying HLA RTI and Java version. In this way, developers could decide the HLA RTI and the Java run-time environment at development-time; and (iii) _Usability_, the complexity of the features provided by the DKF framework are hidden behind an intuitive set of APIs.

The HLA Development Kit is designed, developed, released and managed by the SEI (Systems Engineering and Integration) team, operating in the System Modeling and Simulation Hub (SMASH) Lab of the Department of Informatics, Modeling, Electronics and Systems Engineering (DIMES), University of Calabria (Italy), working in cooperation with NASA JSC (Johnson Space Center), Houston (TX, USA).


## **External links**

* Official website [link](https://smash-lab.github.io/HLA-Development-Kit/);
* DKF all-in-one package [link](https://drive.google.com/open?id=0B6Txsul1iIJmVFdTX0Roc2tGeFk);
* Documentation (API) [link](https://smash-lab.github.io/HLA-Development-Kit/docs/index.html);
* Technical documentations [link](https://smash-lab.github.io/HLA-Development-Kit/document/HLA_Development_Kit_ver_0.0.1.pdf);
* Examples and video-tutorials: How to build a Federate from scratch in 30 minutes! [link](https://drive.google.com/open?id=0B6Txsul1iIJma3pITXE1M2hSOVk);
* The [SEE](http://exploresim.com/) HLA Starter Kit: The SEE-specific extension of the HLA Development Kit [link](https://code.google.com/p/see-hla-starterkit/).

## **Working team**

*  Alfredo Garro, [alfredo.garro@unical.it](mailto:alfredo.garro@unical.it) (coordinator);
*  Alberto Falcone, [alberto.falcone@dimes.unical.it](mailto:alberto.falcone@dimes.unical.it) (main developer); 
*  Andrea Tundis, [andrea.tundis@dimes.unical.it](mailto:andrea.tundis@dimes.unical.it) (developer).

## **Acknowledgments**

The DKF working team would like to thank Edwin Z. Crues (NASA JCS) for his precious advice and suggestions in the development of the HLA Development Kit. A special note of thanks goes also to all the NASA staff involved in the Simulation Exploration Experience (SEE) Project: Priscilla Elfrey, Stephen Paglialonga, Michael Conroy, Dan Dexter, Daniel Oneil, to Björn Möller (PITCH Technologies), and to all the members of SEE teams.

<br>
 
##**References**

1. Anagnostou, A., Chaudhry, N.R., Falcone, A., Garro, A., Salah, O., Taylor, S.J.E., _Easing the development of HLA Federates: the HLA Development Kit and its exploitation in the SEE Project_. In Proc. of the 19th IEEE/ACM International Symposium on Distributed Simulation and Real Time Applications (ACM/IEEE DS-RT), Chengdu, China, October, 14-16, IEEE Computer Society, (2015).

2. Anagnostou, A., Chaudhry, N.R., Falcone, A., Garro, A., Salah, O., Taylor, S.J.E., _A Prototype HLA Development Kit: Results from the 2015 Simulation Exploration Experience_. In Proc. of the ACM SIGSIM PADS 2015, London, UK, June, 10-12, (2015).

3. Bocciarelli, P., D’Ambrogio, A., Falcone, A., Garro, A., Giglio, A., _A model-driven approach to enable the distributed simulation of complex systems_. In Proc. of the 6th Complex Systems Design & Management (CSD&M) 2015, Paris, France, November 23-25, (2015).

4. Falcone, A., Garro, A., Longo, F., Spadafora, F., _SimulationExploration Experience: A Communication System and a 3D Real Time Visualization for a Moon base simulated scenario_. In Proc. of the 18th IEEE/ACM International Symposium on Distributed Simulation and Real Time Applications (ACM/IEEE DS-RT), Toulouse, France, October, 1-3, IEEE Computer Society, (2014).