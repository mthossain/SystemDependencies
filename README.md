# System Dependencies
![Java](https://img.shields.io/badge/Language-Java-orange.svg)
## Project Details 
Components of computer systems often have dependencies—other components that must be in- stalled before they will function properly. These dependencies are frequently shared by multiple components. For example, both the TELNET client program and the FTP client program require that the TCP/IP networking software be installed before they can operate. If you install TCP/IP and the TELNET client program, and later decide to add the FTP client program, you do not need to reinstall TCP/IP.
For some components it would not be a problem if the components on which they depended were reinstalled; it would just waste some resources. But for others, like TCP/IP, some component configuration may be destroyed if the component was reinstalled.
It is useful to be able to remove components that are no longer needed. When this is done, components that only support the removed component may also be removed, freeing up disk space, memory, and other resources. But a supporting component, not explicitly installed, may be removed only if all components which depend on it are also removed. For example, removing the FTP client program and TCP/IP would mean the TELNET client program, which was not removed, would no longer operate. Likewise, removing TCP/IP by itself would cause the failure of both the TELNET and the FTP client programs. Also if we installed TCP/IP to support our own development, then installed the TELNET client (which depends on TCP/IP) and then still later removed the TELNET client, we would not want TCP/IP to be removed.
This program will automate the process of adding and removing components. To do this we will maintain a record of installed components and component dependencies. A component can be installed explicitly in response to a command (unless it is already installed), or implicitly if it is needed for some other component being installed. Likewise, a component, not explicitly installed, can be explicitly removed in response to a command (if it is not needed to support other components) or implicitly removed if it is no longer needed to support another component.

### Sample Input
DEPEND TELNET TCPIP NETCARD

DEPEND TCPIP NETCARD

DEPEND DNS TCPIP NETCARD

DEPEND BROWSER TCPIP HTML

INSTALL NETCARD

INSTALL TELNET

INSTALL foo

REMOVE NETCARD

INSTALL BROWSER

INSTALL DNS

LIST

REMOVE TELNET

REMOVE NETCARD

REMOVE DNS

REMOVE NETCARD

INSTALL NETCARD

REMOVE TCPIP

REMOVE BROWSER

REMOVE TCPIP

END

### Sample Output

DEPEND TELNET TCPIP NETCARD

DEPEND TCPIP NETCARD

DEPEND DNS TCPIP NETCARD

DEPEND BROWSER TCPIP HTML

INSTALL NETCARD

    Installing NETCARD
    
INSTALL TELNET

    Installing TCPIP
    Installing TELNET
    
INSTALL foo

    Installing foo
    
REMOVE NETCARD

    NETCARD is still needed
    
INSTALL BROWSER

    Installing HTML
    Installing BROWSER
    
INSTALL DNS

    Installing DNS
    
LIST

    NETCARD
    TCPIP
    TELNET
    foo
    HTML
    BROWSER
    DNS
    
REMOVE TELNET

    Removing TELNET
    
REMOVE NETCARD

    NETCARD is still needed
    
REMOVE DNS

    Removing DNS
    
REMOVE NETCARD

    NETCARD is still needed
    
INSTALL NETCARD

    NETCARD is already installed
    
REMOVE TCPIP

    TCPIP is still needed
    
REMOVE BROWSER

    Removing BROWSER
    Removing HTML
    Removing TCPIP
    
REMOVE TCPIP

    TCPIP is not installed
    
END

### Sample Input #2: Largest Cycle

DEPEND TELNET TCPIP NETCARD

DEPEND TCPIP NETCARD

DEPEND DNS TCPIP NETCARD

DEPEND BROWSER TCPIP HTML

INSTALL foo

DEPEND NETCARD BROWSER

DEPEND BROWSER2 BROWSER3 TCPIP HTML

DEPEND BROWSER3 BROWSER2

### Sample Output #2: Largest Cycle

DEPEND TELNET TCPIP NETCARD

DEPEND TCPIP NETCARD

DEPEND DNS TCPIP NETCARD

DEPEND BROWSER TCPIP HTML

INSTALL foo

    Installing foo
    
DEPEND NETCARD BROWSER

    Found cycle in dependencies
    Suggest removing DEPEND NETCARD BROWSER
    
DEPEND BROWSER2 BROWSER3 TCPIP HTML

    Found cycle in dependencies
    Suggest removing DEPEND NETCARD BROWSER
    
DEPEND BROWSER3 BROWSER2

    Found cycle in dependencies


## Author
#### M TanVir Hossain

Software Engineer

Sydney, Australia

Email: hossain.tanvir.m@gmail.com

Originally this project was done by me in April, 2017. 
