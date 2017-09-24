    // ILicense.aidl
package com.jdnew.serverapp.aidl;

// Declare any non-default types here with import statements
import com.jdnew.serverapp.aidl.RootLicenseData;




interface ILicense {

   RootLicenseData returnLicense(String licenseData);

   String returnCheckResult(String submitData);

}
