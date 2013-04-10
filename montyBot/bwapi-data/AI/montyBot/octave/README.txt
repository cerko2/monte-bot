This package has been built against the 3.2.4 version of Octave 
available from http://www.octave.org.
It has been built using revision 7124 of the building scripts 
hosted at http://octave.sourceforge.net.

This version of Octave has many new features relative to previous versions, 
including:

  * Compatibility with Matlab graphics has been improved.
  * New experimental OpenGL/FLTK based plotting system.
  * Improvements to the debugger.
  * Improved traceback error messages.
  * Object Oriented Programming.
  * Block comments.
  * Single Precision data type.
  * 64-bit integer arithmetic.
  * New functions for reading and writing images.
  
For an exhausive list of changes and news see the end of this file.

For changes specific to the mingw32 binary of Octave refer to the changelog 
below or the one available at
https://octave.svn.sourceforge.net/svnroot/octave/trunk/octave-forge/admin/Windows/mingw32/CHANGELOG.txt
and the file CHANGELOG.txt in your local installation root.

For known issues of octave on mingw32 platform see the known issues section 
below or the one available at
https://octave.svn.sourceforge.net/svnroot/octave/trunk/octave-forge/admin/Windows/mingw32/KNOWN_ISSUES.txt
and the KNOWN_ISSUES.txt file in your local installation root.

Installing Octave:
------------------

It is highly recommended to install Octave to a path WITHOUT spaces. 
Octave will work if installed to a path including spaces, and all packages 
added at the installation will work if installed into a path containing spaces,
HOWEVER you will currently NOT be able to install packages using octave's
package manager at a later time if Octave was installed to a directory 
containing spaces.

Using the Java interface packages
---------------------------------

The "java" and "jhandles" packages are marked "noauto" as they require a Java
Runtime Environment (JRE) installed when loaded in octave.
The JRE is *not* included in the octave/mingw32 installer. You can get a JRE for 
windows at http://www.java.com. 
To use these packages in octave execute "pkg load java".
To mark them as autoload execute "pkg rebuild -auto java".

Using the xls/ods scripts in the io package
-------------------------------------------

The io package now includes read/write access scripts for Excel/xls and 
OpenOffice/ods spreadsheet files. Depending on the interface chosen, these
scripts depend on additional software.

Accessing Excel/xls files can be done using either the Excel/COM interface or
a Java interface (ApachePOI or JExcelAPI). Access to OpenOffice/ods files is 
implemented using a java interface (either JOpenDocument or odfdom/xerces).

The Excel/COM Interface additionally requires:
   * the windows-1.0.8+ package from the octave/mingw32 installer 
   * An installed version of Excel
   
The Java interface additionally requires:
   * the java-1.2.7+ package from the octave/mingw32 installer
   * a Java Runtime Environment (JRE) >1.6.0 installed

All necessary .jar files for the java interface (except for the JRE) are 
included in the installer.

Please also refer to the documentation of the io package located at
   <path-to-octave>\share\octave\packages\io-1.0.11\doc
specifically the notes on Java memory usage in READ-XLS.html!


Using this package for the development of OCT and MEX files:
-----------------------------------------------------------

This package is bundled with the compiler used to compile this package.
This version of the package has been compiled with Mingw32 gcc using GCC 
version gcc version 4.4.0 (GCC) .
The compiler is available from

http://sourceforge.net/projects/mingw

OCT files can be compiled from the Octave prompt with the "mkoctfile"
command, whereas as MEX files can be compiled with the "mex" command.

Contributing to Octave:
----------------------

There are a number of ways that you can contribute to help make Octave
a better system. Perhaps the most important way to contribute is to
write high-quality code for solving new problems, and to make your
code freely available for others to use. For inspiration, we have a
wish-list of projects (http://www.octave.org/projects.html) and
feature requests.

If you've written a useful function for Octave that you would like to
make available for others to use, please post it to the sources
mailing list (sources@octave.org). Messages sent to this list will be
seen by the Octave maintainers, and will be considered for inclusion
in future releases of Octave. The messages are also archived
(http://www.octave.org/archive.org) , so even if the code is not
included in a future release, it will remain available for others to
use.

If you find Octave useful, consider providing additional funding
(http://www.octave.org/funding.html) to continue its development. Even
a modest amount of additional funding could make a significant
difference in the amount of time that its author can devote to
development and support.

If you cannot provide funding or contribute code, you can still help
make Octave better and more reliable by reporting any bugs
(http://www.octave.org/bugs.html) you find and by offering suggestions
for ways to improve Octave.

Octave-Forge Packages
---------------------

This package is also bundled with a minimum MSYS environment required for
octave's package manager. Provided that the dependencies are met, octave-forge
packages can be installed from the octave prompt with the "pkg" command.

MIND that package installation is currently only possible if Octave is 
installed to a directory WITHOUT spaces!

This package is bundled with the binaries & development files of external
libraries (*except* a Java development kit) allowing the build of the 
following octave-forge packages:
   actuarial-1.1.0
   audio-1.1.4
   benchmark-1.1.1
   bim-1.0.0
   bioinfo-0.1.2
   combinatorics-1.0.9
   communications-1.0.10
   control-1.0.11
   data-smoothing-1.2.0
   econometrics-1.0.8
   fenv-0.1.0
   financial-0.3.2
   fixed-0.7.10
   fpl-1.0.0
   ga-0.9.7
   general-1.2.0
   generate_html-0.1.2
   gnuplot-1.0.1
   gpc-0.1.7
   gsl-1.0.8
   ident-1.0.7
   image-1.0.10
   informationtheory-0.1.8
   integration-1.0.7
   io-1.0.11
   irsa-1.0.7
   java-1.2.7
   jhandles-0.3.5
   linear-algebra-1.0.8
   mapping-1.0.7
   miscellaneous-1.0.9
   missing-functions-1.0.2
   msh-1.0.0
   nlwing2-1.1.1
   nnet-0.1.10
   nurbs-1.0.3
   ocs-0.0.4
   oct2mat-1.0.7
   octcdf-1.0.17
   octgpr-1.1.5
   odebvp-1.0.6
   odepkg-0.6.10
   optim-1.0.12
   optiminterp-0.3.2
   outliers-0.13.9
   physicalconstants-0.1.7
   plot-1.0.7
   quaternion-1.0.0
   signal-1.0.10
   simp-1.1.0
   sockets-1.0.5
   specfun-1.0.8
   special-matrix-1.0.7
   spline-gcvspl-1.0.8
   splines-1.0.7
   statistics-1.0.9
   strings-1.0.7
   struct-1.0.7
   symband-1.0.10
   symbolic-1.0.9
   time-1.0.9
   video-1.0.2
   windows-1.0.8
   zenity-0.5.7

Octave/mingw32 3.2.4 new features, changes and bugfixes (from 3.2.3-2)
====================================================================

*) CHG: add the following packages from octave forge to the installer:
            jhandles-0.3.5+   (noauto)
            java-1.2.7+       (noauto)
            video-1.0.2
            msh-1.0.0
            fpl-1.0.0
            actuarial-1.1.0
            bim-1.0.10
            fenv-0.1.0
            generate_html-0.1.2
            gnuplot-1.0.1
            gpc-0.1.7
            nlwing2-1.1.1
            nurbs-1.0.3
            ocs-0.0.4
            oct2mat-1.0.7
            octgpr-1.1.5
            simp-1.1.0
            spline-gcvspl-1.0.8
            symband-1.0.10

*) NOTE: The following packages are marked "noauto" as they require a Java
         Runtime Environment (JRE) installed when loaded.
            java
            jhandles
         To use these packages execute "pkg load java". Make sure to have a JRE 
         installed.
         To mark them as autoload execute "pkg rebuild -auto java"

*) CHG: updated the following octave forge packages
            windows  1.0.8  -> 1.0.8+    (noauto)
            octcdf   1.0.13 -> 1.0.17+   (noauto)
            optim    1.0.6  -> 1.0.12
            general  1.1.3  -> 1.2.0
            io       1.0.9  -> 1.0.11
            odepkg   0.6.7  -> 0.6.10
        
*) CHG: update gnuplot to 4.4.0
*) CHG: revert PS1 to octave's default (of not including the pwd)
*) CHG: add necessary jars for ods/xls support in the io package

*) NOTE: for reading/writing xls files you can use either:
           - Execl/COM Interface: requires the windows package and Excel 
                                  installed on your cmputer
           - Java Interface: requires the java package and a JRE > 1.6.0 
                             installed on your computer

*) NOTE: reading/writing ods files uses a:
           - Java Interface: requires the java package and a JRE > 1.6.0 
                             installed on your computer

*) NOTE: The JRE is *not* included in the octave/mingw32 installer. You can get 
        a JRE for windows at http://www.java.com.


Octave/mingw32 3.2.3-2 new features, changes and bugfixes (from 3.2.3)
====================================================================

*) CHG: Remove SSE3 ATLAS libraries from installer as they reportedly
        segfault on complex arithmetic. No solution available yet.
*) FIX: Fix a bug in patch of jpeg-7 which caused loading jpeg images to fail
*) FIX: Fix a bug in installer setting wrong shortcuts to octave documentation
*) FIX: Fix missing gnuplot documentation in installer


Octave/mingw32 3.2.3 new features, changes and bugfixes (from 3.2.2)
====================================================================

*) CHG: Build with gcc-4.4.0 from http://www.mingw.org
*) CHG: Update to gnuplot 4.3.0-2009-07-08 CVS snapshot
*) FIX: for gnuplot, add a workaround patch to SF#2848002
*) CHG: build gnuplot with support for wxwidgets terminal. To use it, set the
        environment variable GNUTERM to "wxt", e.g. from within octave execute
            putenv("GNUTERM","wxt")
*) CHG: Build Graphicsmagick as relocatable dll and remove dependency on 
        environment variable defining installation location
*) CHG: Update several dependency libraries
*) CHG: Add SSE/SSE2-enabled fftw3 library
*) CHG: add the fixed-0.7.10 octave forge package to installer
*) FIX: Printing to pdf no longer yields spurious empty pages using gnuplot
*) FIX: Pressing CTRL-C within octave should no longer crash gnuplot
*) FIX: the command "clear all" no longer segfaults after calling eigs()


Octave/mingw32 3.2.0 new features, changes and bugfixes (from 3.2.0)
====================================================================

*) CHG: The 3.2.2 installer includes the following post-3.2.2 changesets in 
	addition to the octave-3.2.2 sources
   
      http://hg.tw-math.de/release-3-2-x/rev/cd95695a0a89  ("dir" crashes)
      http://hg.tw-math.de/release-3-2-x/rev/b308b2e12f04  (16bit images)
      http://hg.tw-math.de/release-3-2-x/rev/9107c882f193  (speed up "plot")
      
   These adress bugfixes relevant to mingw32 platform.
   These will be included by default in succeeding versions of octave/mingw32.

*) CHG: The installer includes a Q16 GraphicsMagick build (configured 
        with --quantum-depth=16) to enable lossless reading/writing of
        16bit images.

*) CHG: The following octave-forge packages have been marked as "-noauto":
      windows-1.0.8
      octcdf-1.0.13
      communications-1.0.10
      ga-0.9.7
   
   This is done to avoid the "clear all" crash if any of these packages
   is selected during the installation. If you require them to be loaded 
   automatically you have to manually set them to, by executing the 
   command(s)
   
      pkg rebuild -auto windows
      pkg rebuild -auto octcdf
      pkg rebuild -auto communications
      pkg rebuild -auto ga
   
   from the octave prompt.
   
   Be advised that this will trigger the "clear all" segfault!

*) FIX: The command "dir" should no longer crash on Windows Vista and Windows 
        Server
*) FIX: Reading/writing images using GraphicsMagick no longer fails with 
        GraphicsMagick complaining about missing configuration files.
*) FIX: Reading/writing 16bit images works without loss of precision


Known Issues with octave/mingw32 3.2.4:
--------------------------------------

*) The command "clear all" results in a segfault.
   This is currently triggered by the following octave-forge packages:
      windows
      octcdf
      communications
      ga (depends on communications)
      
   The error occurs only if any of these packages is marked "-auto" 
   i.e. is loaded at startup.
   
*) Installing octave respectively running octave from a path which contains
   spaces breaks the installation of octave-forge packages using octave's
   package manager pkg.m.
   It this therefore highly recommended to install octave to a path WITHOUT
   spaces. Octave will work if installed to a path including spaces, and all 
   packages added at the installation will work if installed into a path 
   containing spaces, HOWEVER you will currently NOT be able to install 
   packages using octave's package manager at a later time if Octave was 
   installed to a directory containing spaces.

*) "make check" results:

   Summary:
   
    PASS   5754
    FAIL      3
   
   octave-3.2.4\src\data.cc  PASS  506/509  FAIL 3

Summary of important user-visible changes for version 3.2:
---------------------------------------------------------

 ** Compatibility with Matlab graphics has been improved.

    The hggroup object and associated listener callback functions have
    been added allowing the inclusion of group objects.  Data sources
    have been added to these group objects such that

           x = 0:0.1:10;
           y = sin (x);
           plot (x, y, "ydatasource", "y");
           for i = 1 : 100
             pause(0.1)
             y = sin (x + 0.1 * i);
             refreshdata();
           endfor

    works as expected.  This capability has be used to introduce
    stem-series, bar-series, etc., objects for better Matlab
    compatibility.

 ** New graphics functions:

      addlistener         ezcontour   gcbo         refresh  
      addproperty         ezcontourf  ginput       refreshdata
      allchild            ezmesh      gtext        specular
      available_backends  ezmeshc     intwarning   surfl
      backend             ezplot      ishghandle   trisurf
      cla                 ezplot3     isocolors    waitforbuttonpress
      clabel              ezpolar     isonormals  
      comet               ezsurf      isosurface  
      dellistener         findall     linkprop   
      diffuse             gcbf        plotmatrix

 ** New experimental OpenGL/FLTK based plotting system.

    An experimental plotting system based on OpenGL and the FLTK
    toolkit is now part of Octave.  This backend is disabled by
    default.  You can switch to using it with the command

        backend ("fltk")

    for all future figures or for a particular figure with the command

        backend (h, "fltk")

    where "h" is a valid figure handle.  Please note that this backend
    does not yet support text objects.  Obviously, this is a necessary
    feature before it can be considered usable.  We are looking for
    volunteers to help implement this missing feature.

 ** Functions providing direct access to gnuplot have been removed.

    The functions __gnuplot_plot__, __gnuplot_set__, __gnuplot_raw__,
     __gnuplot_show__, __gnuplot_replot__, __gnuplot_splot__,
     __gnuplot_save_data__ and __gnuplot_send_inline_data__ have been
     removed from Octave.  These function were incompatible with the
     high level graphics handle code.

 ** The Control, Finance and Quaternion functions have been removed.

    These functions are now available as separate packages from

      http://octave.sourceforge.net/packages.html

    and can be reinstalled using the Octave package manager (see
    the pkg function).

 ** Specific sparse matrix functions removed.

    The following functions, which handled only sparse matrices have
    been removed.  Instead of calling these functions directly, you
    should use the corresponding function without the "sp" prefix.

      spatan2     spcumsum  spkron   spprod
      spchol      spdet     splchol  spqr
      spchol2inv  spdiag    splu     spsum
      spcholinv   spfind    spmax    spsumsqk
      spcumprod   spinv     spmin

 ** Improvements to the debugger.

    The interactive debugging features have been improved.  Stopping
    on statements with dbstop should work correctly now.  Stepping
    into and over functions, and stepping one statement at a time
    (with dbstep) now works.  Moving up and down the call stack with
    dbup and dbdown now works.  The dbstack function is now available
    to print the current function call stack.  The new dbquit function
    is available to exit the debugging mode.

 ** Improved traceback error messages.

    Traceback error messages are much more concise and easier to
    understand.  They now display information about the function call
    stack instead of the stack of all statements that were active at
    the point of the error.

 ** Object Oriented Programming.

    Octave now includes OOP features and the user can create their own
    class objects and overloaded functions and operators.  For
    example, all methods of a class called "myclass" will be found in
    a directory "@myclass" on the users path.  The class specific
    versions of functions and operators take precedence over the
    generic versions of these functions.

    New functions related to OOP include

      class  inferiorto  isobject  loadobj  methods  superiorto

    See the Octave manual for more details.

 ** Parsing of Command-style Functions.

    Octave now parses command-style functions without needing to first
    declare them with "mark_as_command".  The rules for recognizing a
    command-style function calls are

      * A command must appear as the first word in a statement,
        followed by a space.

      * The first character after the space must not be '=' or '('

      * The next token after the space must not look like a binary
        operator.

    These rules should be mostly compatible with the way Matlab parses
    command-style function calls and allow users to define commands in
    .m files without having to mark them as commands.

    Note that previous versions of Octave allowed expressions like

      x = load -text foo.dat

    but an expression like this will now generate a parse error.  In
    order to assign the value returned by a function to a variable,
    you must use the normal function call syntax:

      x = load ("-text", "foo.dat");

 ** Block comments.

    Commented code can be between matching "#{" and "#}" or "%{" and
    "%}" markers, even if the commented code spans several line.  This
    allows blocks code to be commented, without needing to comment
    each line.  For example,

    function [s, t] = func (x, y)
      s = 2 * x;
    #{
      s *= y;
      t = y + x;
    #}
    endfunction

    the lines "s *= y;" and "t = y + x" will not be executed.

 ** Special treatment in the parser of expressions like "a' * b".

    In these cases the transpose is no longer explicitly formed and
    BLAS libraries are called with the transpose flagged,
    significantly improving performance for these kinds of
    operations.

 ** Single Precision data type.

    Octave now includes a single precision data type.  Single
    precision variables can be created with the "single" command, or
    from functions like ones, eye, etc.  For example,

      single (1)
      ones (2, 2, "single")
      zeros (2, 2, "single")
      eye (2, 2, "single")
      Inf (2, 2, "single")
      NaN (2, 2, "single")
      NA (2, 2, "single")

    all create single precision variables.  For compatibility with
    Matlab, mixed double/single precision operators and functions
    return single precision types.

    As a consequence of this addition to Octave the internal
    representation of the double precision NA value has changed, and
    so users that make use of data generated by Octave with R or
    visa-versa are warned that compatibility might not be assured.

 ** Improved array indexing.

    The underlying code used for indexing of arrays has been
    completely rewritten and indexing is now significantly faster.

 ** Improved memory management.

    Octave will now attempt to share data in some cases where previously
    a copy would be made, such as certain array slicing operations or
    conversions between cells, structs and cs-lists.  This usually reduces
    both time and memory consumption.
    Also, Octave will now attempt to detect and optimize usage of a vector 
    as a stack, when elements are being repeatedly inserted at/removed from 
    the end of the vector.

 ** Improved performance for reduction operations.

    The performance of the sum, prod, sumsq, cumsum, cumprod, any, all,
    max and min functions has been significantly improved.

 ** Sorting and searching.
    
    The performance of sort has been improved, especially when sorting
    indices are requested. An efficient built-in issorted implementation
    was added. sortrows now uses a more efficient algorithm, especially
    in the homegeneous case. lookup is now a built-in function performing
    a binary search, optimized for long runs of close elements. Lookup
    also works with cell arrays of strings.

 ** Range arithmetics

    For some operations on ranges, Octave will attempt to keep the result as a
    range.  These include negation, adding a scalar, subtracting a scalar, and
    multiplying by a scalar. Ranges with zero increment are allowed and can be
    constructed using the built-in function `ones'.

 ** Various performance improvements.

    Performance of a number of other built-in operations and functions was
    improved, including:

    * logical operations
    * comparison operators
    * element-wise power
    * accumarray
    * cellfun
    * isnan
    * isinf
    * isfinite
    * nchoosek
    * repmat
    * strcmp

 ** 64-bit integer arithmetic.

    Arithmetic with 64-bit integers (int64 and uint64 types) is fully
    supported, with saturation semantics like the other integer types.
    Performance of most integer arithmetic operations has been
    improved by using integer arithmetic directly.  Previously, Octave
    performed integer math with saturation semantics by converting the
    operands to double precision, performing the operation, and then
    converting the result back to an integer value, truncating if
    necessary.

 ** Diagonal and permutation matrices.

    The interpreter can now treat diagonal and permutation matrices as
    special objects that store only the non-zero elements, rather than
    general full matrices.  Therefore, it is now possible to construct
    and use these matrices in linear algebra without suffering a
    performance penalty due to storing large numbers of zero elements.

 ** Improvements to fsolve.

    The fsolve function now accepts an option structure argument (see
    also the optimset function).  The INFO values returned from fsolve
    have changed to be compatible with Matlab's fsolve function.
    Additionally, fsolve is now able to solve overdetermined systems,
    complex-differentiable complex systems, systems with a sparse
    jacobian and can work in single precision if given single precision
    inputs.  It can also be called recursively.

 ** Improvements to the norm function.

    The norm function is now able to compute row or column norms of a
    matrix in a single call, as well as general matrix p-norms.

 ** New functions for computing some eigenvalues or singular values.

    The eigs and svds functions have been included in Octave.  These
    functions require the ARPACK library (now distributed under a
    GPL-compatible license).

 ** New QR and Cholesky factorization updating functions.

      choldelete  cholshift   qrdelete  qrshift
      cholinsert  cholupdate  qrinsert  qrupdate

 ** New quadrature functions.

      dblquad  quadgk  quadv  triplequad

 ** New functions for reading and writing images.

    The imwrite and imread functions have been included in Octave.
    These functions require the GraphicsMagick library.  The new
    function imfinfo provides information about an image file (size,
    type, colors, etc.)

 ** The input_event_hook function has been replaced by the pair of
    functions add_input_event_hook and remove_input_event_hook so that
    more than one hook function may be installed at a time.

 ** Other miscellaneous new functions.

      addtodate          hypot                       reallog
      bicgstab           idivide                     realpow
      cellslices         info                        realsqrt
      cgs                interp1q                    rectint
      command_line_path  isdebugmode                 regexptranslate
      contrast           isfloat                     restoredefaultpath
      convn              isstrprop                   roundb
      cummin             log1p                       rundemos
      cummax             lsqnonneg                   runlength
      datetick           matlabroot                  saveobj
      display            namelengthmax               spaugment
      expm1              nargoutchk                  strchr
      filemarker         pathdef                     strvcat
      fstat              perl                        subspace
      full               prctile                     symvar
      fzero              quantile                    treelayout
      genvarname         re_read_readline_init_file  validatestring
      histc

 ** Changes to strcat.

    The strcat function is now compatible with Matlab's strcat
    function, which removes trailing whitespace when concatenating
    character strings.  For example

      strcat ('foo ', 'bar')
      ==> 'foobar'

    The new function cstrcat provides the previous behavior of
    Octave's strcat.

 ** Improvements to the help functions.

    The help system has been mostly re-implemented in .m files to make
    it easier to modify.  Performance of the lookfor function has been
    greatly improved by caching the help text from all functions that
    are distributed with Octave.  The pkg function has been modified
    to generate cache files for external packages when they are
    installed.

 ** Deprecated functions.

    The following functions were deprecated in Octave 3.0 and will be
    removed in Octave 3.4 (or whatever version is the second major
    release after 3.0):
                                           
      beta_cdf         geometric_pdf        pascal_pdf      
      beta_inv         geometric_rnd        pascal_rnd      
      beta_pdf         hypergeometric_cdf   poisson_cdf     
      beta_rnd         hypergeometric_inv   poisson_inv     
      binomial_cdf     hypergeometric_pdf   poisson_pdf     
      binomial_inv     hypergeometric_rnd   poisson_rnd     
      binomial_pdf     intersection         polyinteg       
      binomial_rnd     is_bool              setstr          
      chisquare_cdf    is_complex           struct_contains 
      chisquare_inv    is_list              struct_elements 
      chisquare_pdf    is_matrix            t_cdf           
      chisquare_rnd    is_scalar            t_inv           
      clearplot        is_square            t_pdf           
      clg              is_stream            t_rnd           
      com2str          is_struct            uniform_cdf     
      exponential_cdf  is_symmetric         uniform_inv     
      exponential_inv  is_vector            uniform_pdf     
      exponential_pdf  isstr                uniform_rnd     
      exponential_rnd  lognormal_cdf        weibcdf         
      f_cdf            lognormal_inv        weibinv         
      f_inv            lognormal_pdf        weibpdf         
      f_pdf            lognormal_rnd        weibrnd         
      f_rnd            meshdom              weibull_cdf     
      gamma_cdf        normal_cdf           weibull_inv     
      gamma_inv        normal_inv           weibull_pdf     
      gamma_pdf        normal_pdf           weibull_rnd     
      gamma_rnd        normal_rnd           wiener_rnd      
      geometric_cdf    pascal_cdf
      geometric_inv    pascal_inv

    The following functions are now deprecated in Octave 3.2 and will
    be removed in Octave 3.6 (or whatever version is the second major
    release after 3.2):

      create_set          spcholinv  spmax
      dmult               spcumprod  spmin
      iscommand           spcumsum   spprod
      israwcommand        spdet      spqr
      lchol               spdiag     spsum
      loadimage           spfind     spsumsq
      mark_as_command     spinv      str2mat
      mark_as_rawcommand  spkron     unmark_command
      spatan2             splchol    unmark_rawcommand
      spchol              split
      spchol2inv          splu

See NEWS.3 for old news.
