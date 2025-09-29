# 🏡 RealEstate Analyser

**RealEstate Analyser** is an AI-powered Spring Boot application designed to help users analyze the **current status of real estate listings**, evaluate pricing, and identify **good deals** by labeling properties according to their value.  

---

## 🚀 Features

- 🔍 **AI-driven analysis**: Uses machine learning models to evaluate real estate listings based on features such as price, location, amenities, and market trends.  
- 🏷️ **Categorization**: Labels properties as `Good 🟢`, `Fair 🟡`, or `Poor 🔴` based on AI evaluation.  
- 🗄️ **Database support**: Works with PostgreSQL to store and query listings efficiently.  
- 🌐 **Web interface ready**: Exposes REST endpoints for integrating with web or mobile apps.  
- ⚙️ **Extensible**: Easily extend AI models or scoring rules for more detailed analysis.  
- 📈 **Metrics & monitoring**: Integrated with Spring Boot Actuator for application health and usage metrics.  

---

## 🛠️ Tech Stack

- **Backend**: Spring Boot, Open API, Spring Data JPA, OAUTH2, HazelCast and a few others magic tricks 😉  
- **Database**: PostgreSQL
- **AI Integration**: OpenAI API (via OpenAI API)
- **Web scraping:** Jsoup (HTML parsing) and Selenium / Playwright (for JS-heavy pages)
- **Monitoring**: Spring Boot Actuator + Prometheus.
- **Build Tool**: Maven (with Maven Wrapper included)  

---
## 🕸️ Data Collection & Integration

**RealEstate Analyser** can optionally gather real estate listing data from multiple sources to enhance its analysis.  

> ⚠️ All data collection is intended to be **legal and ethical**. Users of this tool are responsible for respecting website terms, copyright, and privacy laws when integrating external data. Always prefer **official APIs** when available.

### Features
- Aggregate property listings from multiple sources to build richer datasets.  
- Normalize property information for consistent scoring (price, location, amenities).  
- Optionally combine AI predictions with collected data to identify **good deals**.  

### Guidelines
- Only collect data you are legally permitted to access.  
- Respect `robots.txt` directives and rate limits.  
- Store data responsibly, and ensure sensitive information is not included.  
- Prefer sources that offer public APIs or data partnerships.  

> This ensures your use of RealEstate Analyser is compliant, professional, and ethical while still enabling powerful AI-driven insights.

> ⚠️ **Important:** The scraping/data collection feature is **not yet available** and will be implemented in future versions. This ensures the current release is stable, compliant, and fully functional without scraping.

## ⚡ Quick Start

1. **Clone the repository**
```bash
git clone https://github.com/manuelondina/realestate-analyser.git
cd realestate-analyser
