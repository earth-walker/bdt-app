export default {
  benefits: {
    phlLoop: {
      eligibility: {
        result: true,
        checks: {
          tenOrMoreYearsOwnerOccupant: true,
          notEnrolledInPhlLoop: true,
          noTenYearTaxAbatement: true,
          loopTaxAssessmentEligible: true,
          phillyOwnerOccupantHomeowner: true,
          underPreviousAnnualGrossIncome: true,
          notTaxDelinquentOrIsEnrolledInOopaEligible: true,
        },
      },
      info: "The Longtime Owner Occupants Program (LOOP) is a Real Estate Tax relief program for eligible homeowners whose property assessments increased by at least 50% from last year or increased by at least 75% in the last five years.",
      appLink:
        "https://www.phila.gov/documents/longtime-owner-occupants-program-loop-forms/",
    },
    phlSeniorCitizenTaxFreeze: {
      eligibility: {
        result: null,
        checks: {
          notEnrolledInPhlSeniorCitizenTaxFreeze: true,
          meetsPhlSeniorCitizenTaxFreezeAgeRequirements: null,
          phillyOwnerOccupantHomeowner: true,
          underMaritalStatusBasedCurrentMonthlyGrossIncome: null,
        },
      },
      info: "Quo qui minus nisi facilis dolorem ullam ex. Perspiciatis expedita doloremque ratione esse non iusto. Eos qui dolore quisquam ipsum vel. Dolores dignissimos quibusdam qui tempora et autem dolore. Accusantium saepe praesentium et dignissimos veniam perspiciatis magni quam.",
      appLink:
        "https://www.phila.gov/documents/longtime-owner-occupants-program-loop-forms/",
    },
    phlLowIncomeTaxFreeze: {
      eligibility: {
        result: false,
        checks: {
          phillyOwnerOccupantHomeowner: false,
          notEnrolledInPhlLowIncomeTaxFreeze: null,
          underMaritalStatusBasedCurrentMonthlyGrossIncome: null,
        },
      },
      info: "Quo qui minus nisi facilis dolorem ullam ex. Perspiciatis expedita doloremque ratione esse non iusto. Eos qui dolore quisquam ipsum vel. Dolores dignissimos quibusdam qui tempora et autem dolore. Accusantium saepe praesentium et dignissimos veniam perspiciatis magni quam.",
      appLink:
        "https://www.phila.gov/documents/longtime-owner-occupants-program-loop-forms/",
    },
    phlOopa: {
      eligibility: {
        result: false,
        checks: {
          taxDelinquent: null,
          phillyOwnerOccupantHomeowner: false,
          notEnrolledInPhlOopa: null,
        },
      },
      info: "Quo qui minus nisi facilis dolorem ullam ex. Perspiciatis expedita doloremque ratione esse non iusto. Eos qui dolore quisquam ipsum vel. Dolores dignissimos quibusdam qui tempora et autem dolore. Accusantium saepe praesentium et dignissimos veniam perspiciatis magni quam.",
      appLink:
        "https://www.phila.gov/documents/longtime-owner-occupants-program-loop-forms/",
    },
    phlHomesteadExemption: {
      eligibility: {
        result: false,
        checks: {
          noTenYearTaxAbatement: false,
          phillyOwnerOccupantHomeowner: false,
          notEnrolledInPhlHomesteadExemption: false,
        },
      },
      info: "Quo qui minus nisi facilis dolorem ullam ex. Perspiciatis expedita doloremque ratione esse non iusto. Eos qui dolore quisquam ipsum vel. Dolores dignissimos quibusdam qui tempora et autem dolore. Accusantium saepe praesentium et dignissimos veniam perspiciatis magni quam.",
      appLink:
        "https://www.phila.gov/documents/longtime-owner-occupants-program-loop-forms/",
    },
  },
  branding: {
    orgName: "Organization Name",
    authorInfo: "Created by Org Name",
  },
};
